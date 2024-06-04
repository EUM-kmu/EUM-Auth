package com.eum.auth.controller;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.common.DTO.enums.SuccessCode;
import com.eum.auth.controller.DTO.request.FcmTokenRequest;
import com.eum.auth.controller.DTO.request.UsersRequest;
import com.eum.auth.domain.user.SocialType;
import com.eum.auth.domain.user.User;
import com.eum.auth.service.AuthService;
import com.eum.auth.service.FcmService;
import com.eum.auth.service.KakaoService;
import com.eum.auth.service.UsersService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth-service/api/v2")
@RestController
@CrossOrigin(origins = {"http://localhost:3000","https://hanmaeul.vercel.app","https://k-eum2023.web.app"}, allowedHeaders = "*")
public class UserController {
    private final UsersService usersService;
    private final KakaoService kakaoService;
    private final AuthService authService;
    private final FcmService fcmService;
    @Hidden
    @Transactional
    @PostMapping("/withdrawal")
    public void withdrawal(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestHeader("userId") String userId) throws FirebaseAuthException {
        User getUser = usersService.findById(Long.valueOf(userId));
        usersService.withdrawal(getUser);
//        소셜 로그인 타입별 정보 제거
        if(getUser.getSocialType() == SocialType.KAKAO){
            kakaoService.WithdralKakao(getUser.getUid()); //카카오와 연결 끊기
        } else if (getUser.getSocialType() == SocialType.FIREBASE) {
            FirebaseAuth.getInstance().deleteUser(getUser.getUid()); //파이어베이스에 저장된 유저정보 제거
        }
        String bearerToken = extractBearerToken(authorizationHeader);
        authService.logout(bearerToken);
    }
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    @PostMapping("notification")
    public ResponseEntity<APIResponse> updateFcm(@RequestBody FcmTokenRequest token, @RequestHeader("userId") String userId){
        if(!token.getFcmToken().equals("")) fcmService.updateFcmToken( Long.valueOf(userId),token.getFcmToken());
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS, "fcm 토큰 등록"));
    }



}
