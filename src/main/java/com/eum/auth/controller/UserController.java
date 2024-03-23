package com.eum.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth-service/api/v2")
@RestController
@CrossOrigin("*")
public class UserController {


//    /**
//     *
//     * @param authorizationHeader : redis에 유저 정보(customUserDetails)을 삭제하기 위해 받는 헤더 정보
//     * @param withdrawal : 탈퇴 사유를 위한 body
//     * @param customUserDetails : jwt 토큰에 담겨있는 customUserDetails
//     * @return
//     * @throws FirebaseAuthException
//     *
//     * 로직 : 계좌 동결, 탈퇴 유저와의 채팅 block 처리, 유저정보 빈값넣기, 지원 취소 시키기
//     */
//    @PostMapping("/withdrawal")
//    public ResponseEntity<APIResponse> withdrawal(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody UsersRequestDTO.Withdrawal withdrawal,@AuthenticationPrincipal CustomUserDetails customUserDetails) throws FirebaseAuthException {
//        Users getUser = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
//        bankAccountService.freezeAccount(getUser); //계좌 동결
//        chatService.blockedChatInWithdrawal(getUser); //탈퇴 유저와의 채팅 block
//        applyService.withdrawalApply(getUser); //탈퇴 유저 지원취소 처리
//
////        소셜 로그인 타입별 정보 제거
//        if(getUser.getSocialType() == SocialType.KAKAO){
//            kakaoService.WithdralKakao(getUser.getUid()); //카카오와 연결 끊기
//        } else if (getUser.getSocialType() == SocialType.FIREBASE) {
//            FirebaseAuth.getInstance().deleteUser(getUser.getUid()); //파이어베이스에 저장된 유저정보 제거
//        }
//        usersService.withdrawal(withdrawal,getUser); //탈퇴 사유 저장
//        String bearerToken = extractBearerToken(authorizationHeader);
//        usersService.logout(bearerToken);
//        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS, "탈퇴성공"));
//    }
//
//    /**
//     * 차단하기 , 차단해제 같은 controller에 담은 -> 프론트 요청으로 다른 컨트롤러로 분리해야함
//     * @param blockedAction : 차단할 유저 id
//     * @param customUserDetails : jwt에 담긴 customUserDetails
//     * @return
//     */
//    @PostMapping("/block")
//    public ResponseEntity<APIResponse> blockedAction(@RequestBody UsersRequestDTO.BlockedAction blockedAction, @AuthenticationPrincipal CustomUserDetails customUserDetails){
//        Users blocker = usersService.findById(Long.valueOf(customUserDetails.getUsername()));
//        Users blocked = usersService.findById(blockedAction.getUserId()); //차단할 유저 객체
//
//        Boolean isBlocked = blockService.blockedAction(blocker, blocked); //차단•해제에 대한 판별
//        chatService.blockedAction(isBlocked,blocker, blocked); // 차단한/된 유저와의 채팅 block
//        applyService.blockedAction( blocker, blocked); // 지원취소
//
//        String msg = isBlocked ? "차단 성공" : "차단 해제";
//        return new ResponseEntity<>(APIResponse.of(SuccessCode.INSERT_SUCCESS,msg),HttpStatus.CREATED);
//
//
//    }

}
