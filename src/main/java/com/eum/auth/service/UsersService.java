package com.eum.auth.service;

import com.eum.auth.domain.user.Role;
import com.eum.auth.domain.user.SocialType;
import com.eum.auth.domain.user.User;
import com.eum.auth.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UsersService {
    private final UserRepository userRepository;


    public User findById(Long userId){
        User getUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("invalid email"));
        return getUser;
    }
    public void withdrawal(User user){
        user.setDeleted();
        user.updateRole(Role.ROLE_SLEEPER);
        userRepository.save(user);
    }
    public User createTemporaryUser(String uid, SocialType socialType){
        return userRepository.save(User.toEntity(uid, socialType));
    }

    public User rejoinUser(User deletedUser){
        User rejoinUser = createTemporaryUser(deletedUser.getUid(), deletedUser.getSocialType()); //새유저 생성
        rejoinUser.setPreviousUserId(deletedUser.getUserId()); //기존 유저 id 받기
        userRepository.save(deletedUser);
        deletedUser.setUid(""); //기존 유저 uid 제거
        return userRepository.save(rejoinUser);
    }
}
