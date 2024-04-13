//package com.eum.auth.controller;
//
//import com.eum.auth.common.DTO.APIResponse;
//import com.eum.auth.controller.DTO.response.UserResponse;
//import com.eum.auth.domain.user.SocialType;
//import com.eum.auth.service.DTO.KakaoDTO;
//import com.eum.auth.service.FirebaseAuthService;
//import com.eum.auth.service.KakaoService;
//import com.eum.auth.service.UsersService;
//import com.google.firebase.auth.FirebaseAuthException;
//
//import io.swagger.v3.oas.annotations.Hidden;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@Hidden
//@CrossOrigin("*")
//public class KakaoController {
//    @Autowired
//    private KakaoService kakaoService;
//    @Autowired
//    private UsersService usersService;
//    private final FirebaseAuthService firebaseAuthService;
//
//    //    테스트용 카카오 로그인 사용 x
//
//    @GetMapping("auth-service/api/v2/auth")
//    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> getToken(@RequestParam String code) throws IOException, FirebaseAuthException {
//        String access = kakaoService.getKakaoAccessT(code);
//        KakaoDTO.KaKaoInfo kaKaoInfo = kakaoService.createKakaoUser(access);
////        log.info(access);
////        return ResponseEntity.ok(usersService.getToken(kaKaoInfo.getEmail(), kaKaoInfo.getUid(), SocialType.KAKAO));
//        SocialType socialType = SocialType.KAKAO;
//        String email = kaKaoInfo.getEmail();
//        String uid = kaKaoInfo.getUid();
//        log.info(firebaseAuthService.checkAuth(email).toString());
//        if (firebaseAuthService.checkAuth(email)) {
//            return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
//        } else {
//            firebaseAuthService.createAccountInFirebase(email,uid);
//            return ResponseEntity.ok(usersService.getToken(email, uid, socialType));
//        }
//    }
//
//
//}