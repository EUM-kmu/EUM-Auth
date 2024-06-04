package com.eum.auth.service;

import com.eum.auth.domain.fcmtoken.FcmToken;
import com.eum.auth.domain.fcmtoken.FcmTokenRepository;
import com.eum.auth.domain.user.User;
import com.eum.auth.domain.user.UserRepository;
import com.eum.auth.messageq.FcmDeletedProducer;
import com.eum.auth.messageq.FcmProducer;
import com.eum.auth.service.DTO.FcmTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmDeletedProducer fcmDeletedProducer;
    private final FcmProducer fcmProducer;
    private final UsersService usersService;
    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    public void updateFcmToken(Long userId,String token){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("invalid userId"));

        FcmToken fcmToken = fcmTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(token); // 기존 토큰 값을 새로운 토큰 값으로 업데이트
                    return fcmTokenRepository.save(existingToken); // 업데이트된 토큰을 저장
                })
                .orElseGet(() -> {
                    FcmToken newToken = FcmToken.builder().user(user).token(token).build();
                    return fcmTokenRepository.save(newToken); // 새로 생성된 토큰을 저장
                });

        FcmTokenDTO.FCMDTO fcmdto = new FcmTokenDTO.FCMDTO(userId, fcmToken.getToken());
        fcmProducer.send(fcmdto);
    }

    public void deleteFcmToken( Long userId){
        User user = usersService.findById(userId);

        FcmTokenDTO.deletedToken deletedToken = new FcmTokenDTO.deletedToken(user.getUserId());
        fcmDeletedProducer.send(deletedToken);

    }


}
