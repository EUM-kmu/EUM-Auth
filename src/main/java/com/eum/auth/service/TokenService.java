package com.eum.auth.service;

import com.eum.auth.config.jwt.JwtTokenProvider;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import com.eum.auth.controller.DTO.response.UserResponse;
import com.eum.auth.domain.CustomUserInfoDto;
import com.eum.auth.domain.user.Role;
import com.eum.auth.domain.user.SocialType;
import com.eum.auth.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UsersService usersService;
    private final ModelMapper modelMapper;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final ProfileService profileService;
    public UserResponse.TokenInfo newUser(String uid, SocialType socialType){
        User temporaryUser = usersService.createTemporaryUser(uid, socialType);
        CustomUserInfoDto info = modelMapper.map(temporaryUser, CustomUserInfoDto.class);
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(info,Role.ROLE_TEMPORARY_USER);
        tokenInfo.setUserId(temporaryUser.getUserId());
        return tokenInfo;
    }
    public UserResponse.TokenInfo temporaryToken(User user){
        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(info, Role.ROLE_TEMPORARY_USER);
        return tokenInfo;
    }
    public UserResponse.TokenInfo userToken(User user){
        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(info,Role.ROLE_USER);
        ProfileResponseDTO.ProfileResponse profileResponse = profileService.getProfile(String.valueOf(user.getUserId()));
        tokenInfo.setNickName(profileResponse.getNickName());
        return tokenInfo;
    }
    public UserResponse.TokenInfo rejoinToken(User deletedUser){
        User newUser = usersService.rejoinUser(deletedUser); //새 유저 생성
        CustomUserInfoDto info = modelMapper.map(newUser, CustomUserInfoDto.class);
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(info, Role.ROLE_TEMPORARY_USER);
        tokenInfo.setUserId(newUser.getUserId());
        return tokenInfo;
    }
}
