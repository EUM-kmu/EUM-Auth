package com.eum.auth.service;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.common.DTO.enums.SuccessCode;
import com.eum.auth.config.jwt.JwtTokenProvider;
import com.eum.auth.controller.DTO.request.UsersRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import com.eum.auth.controller.DTO.response.UserResponse;
import com.eum.auth.domain.CustomUserInfoDto;
import com.eum.auth.domain.user.Role;
import com.eum.auth.domain.user.SocialType;
import com.eum.auth.domain.user.User;
import com.eum.auth.domain.user.UserRepository;
import com.eum.auth.exception.TokenException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final ModelMapper modelMapper;
    private final ProfileService profileService;
    private final TokenService tokenService;


    /**
     * 자체 로그인
     * @param signIn
     * @return
     */
    public UserResponse.TokenInfo signIn(UsersRequest.SignIn signIn) {
        User getUser = userRepository.findByEmail(signIn.getTestmail()).orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 정보"));
        if(!passwordEncoder.matches(signIn.getPassword(),getUser.getPassword())) throw new IllegalArgumentException("잘못된 비밀번호");
        CustomUserInfoDto userInfo = modelMapper.map(getUser, CustomUserInfoDto.class);
        UserResponse.TokenInfo  tokenInfo = jwtTokenProvider.generateToken(userInfo,getUser.getRole());
        tokenInfo.setUserId(getUser.getUserId());
        if(getUser.getRole() == Role.ROLE_USER){
            ProfileResponseDTO.ProfileResponse profileResponse = profileService.getProfile(String.valueOf(getUser.getUserId()));
            tokenInfo.setNickName(profileResponse.getNickName());
        }
        // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + getUser.getUserId(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return tokenInfo;
    }

    /**
     * 토큰 경신
     * @param reissue
     * @return
     */
    public APIResponse<UserResponse.TokenInfo> reissue(UsersRequest.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            throw new TokenException("Refresh Token 정보가 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new TokenException("Redis 에 RefreshToken 이 존재하지 않습니다");
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            throw new TokenException("refresh정보가 일치 하지 않습니다");
        }

        // 4. 새로운 토큰 생성
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        User getUser = userRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(() -> new IllegalArgumentException("잘못된 토큰"));
        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        tokenInfo.setRejoinUser(getUser.getPreviousUserId() != -1);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS,tokenInfo);
    }

    /**
     * 로그이웃
     * @param token
     */
    public void logout(String token) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new TokenException("잘못된 토큰 입니다");
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue()
                .set(token, "logout", expiration, TimeUnit.MILLISECONDS);

    }

    /**
     * 자체 jwt 토큰 발급
     * @param uid
     * @param socialType
     * @return
     */
    public UserResponse.TokenInfo getToken(String uid, SocialType socialType){
        UserResponse.TokenInfo tokenInfo = null;
        if(userRepository.existsByUid(uid)){
            User getUser = userRepository.findByUid(uid).get();
            if(getUser.isDeleted()){
                tokenInfo = tokenService.rejoinToken(getUser);
            }else {
                if(userRepository.existsByUidAndRole(uid,Role.ROLE_USER)){ //활동 가능한 유저
                    tokenInfo = tokenService.userToken(getUser);
                } else if (userRepository.existsByUidAndRole(uid,Role.ROLE_TEMPORARY_USER) ) {
                    tokenInfo = tokenService.temporaryToken(getUser);
                }
            }
        }else{ //이메일이 없으면 최초 가입 유저 == 프로필이 없는 상태
            tokenInfo = tokenService.newUser(uid,socialType);
        }
        redisTemplate.opsForValue()
                .set("RT:" +tokenInfo.getUserId(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return tokenInfo;
    }


    public UserResponse.TokenInfo updateRoleAndToken(Role role, Long userId){
        User getUser = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NullPointerException("invalid userId"));
        getUser.updateRole(role);
        userRepository.save(getUser);

        CustomUserInfoDto userInfo = modelMapper.map(getUser, CustomUserInfoDto.class);
        UserResponse.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userInfo,getUser.getRole());
        redisTemplate.opsForValue()
                .set("RT:" + getUser.getUserId(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
        return tokenInfo;
    }
    //재가입 유저



}
