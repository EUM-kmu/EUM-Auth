package com.eum.auth.controller;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.common.DTO.enums.SuccessCode;
import com.eum.auth.controller.DTO.enums.SignInType;
import com.eum.auth.controller.DTO.request.UsersRequest;
import com.eum.auth.controller.DTO.response.UserResponse;
import com.eum.auth.domain.CustomUserDetails;
import com.eum.auth.domain.user.Role;
import com.eum.auth.domain.user.SocialType;
import com.eum.auth.service.DTO.KakaoDTO;
import com.eum.auth.service.KakaoService;
import com.eum.auth.service.UsersService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth-service/api/v2")
@RestController
@Tag(name = "Auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AuthController {
    private final UsersService usersService;
    private final KakaoService kakaoService;
    @Hidden
    @GetMapping()
    public String get(){
        return "ok";
    }


    /**
     *
     * @param customUserDetails : jwt 토큰에 담겨있는 customUserDetails
     * @return :
     * 기능 :
     *  프로필 작성 중단하고 서비스를 재사용할때 상태 판별을 위한 토큰 조회 controller
     *  ROLE_UNPROFILE_USER : 새유저, 소셜로그인만 한 상태
     *  ROLE_UNPASSWORD_USER : 프로필만 만들고 계좌 생성이 안된 상태
     *  ROLE_USER : 최종 활동이 가능한 상태
     */
    @Hidden
    @GetMapping("/token")
    public ResponseEntity<APIResponse<UserResponse.UserRole>> validateToken(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return new ResponseEntity<>(usersService.validateToken(Long.valueOf(customUserDetails.getUsername())), HttpStatus.OK);
    }
    @Hidden
    @PutMapping("/token")
    public UserResponse.TokenInfo updateToken(@RequestHeader("userId") String userId) {
        UserResponse.TokenInfo tokenInfo = usersService.updateRoleAndToken(Role.ROLE_USER, Long.valueOf(userId));
//        APIResponse response = APIResponse.of(SuccessCode.UPDATE_SUCCESS, tokenInfo);
        return tokenInfo;
    }


    /**
     *
     * @param signIn  : email, password
     * @return : jwt 토큰
     */

    @PostMapping("/auth/signin/local")
    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> signIn(@RequestBody @Validated UsersRequest.SignIn signIn){
        return ResponseEntity.ok(usersService.signIn(signIn));
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> reissue(@RequestBody @Validated UsersRequest.Reissue reissue){
        return ResponseEntity.ok(usersService.reissue(reissue));
    }

    /**
     *
     * @param authorizationHeader : 헤더에 들어온 토큰
     * @return
     * 기능 : redis에서 토큰 정보 삭제
     */

    @PostMapping("/logOut")
    public ResponseEntity<?> logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Extract Bearer token from Authorization header
        String bearerToken = extractBearerToken(authorizationHeader);
        usersService.logout(bearerToken);

        // Pass the Bearer token to the logout method
        return ResponseEntity.ok( APIResponse.of(SuccessCode.UPDATE_SUCCESS,"로그아웃 되었습니다."));
    }

    /**
     *
     * @param type : kakao,firebase
     * @param token : 프론트에서 넘어오는 소셜로그인 토큰
     * @return : jwt 토큰
     * @throws IOException
     * @throws FirebaseAuthException
     */

    @PostMapping("/auth/signin/{type}")
    public ResponseEntity<APIResponse<UserResponse.TokenInfo>> getToken(@PathVariable SignInType type , @RequestBody @Validated UsersRequest.Token token) throws IOException, FirebaseAuthException {
        String email = "";
        String uid = "";
        SocialType socialType = null;
        if(type == SignInType.kakao){
            KakaoDTO.KaKaoInfo kaKaoInfo= kakaoService.createKakaoUser(token.getToken());
            email = kaKaoInfo.getEmail();
            uid = kaKaoInfo.getUid();
            socialType = SocialType.KAKAO;
        } else if (type == SignInType.firebase) {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token.getToken());
            email = firebaseToken.getEmail();
            uid = firebaseToken.getUid();
            socialType = SocialType.FIREBASE;
        }
//        log.info(token.);
        return ResponseEntity.ok(usersService.getToken(email,uid,socialType));
    }

    // 헤더에서 토큰 추출 위한 코드
    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
