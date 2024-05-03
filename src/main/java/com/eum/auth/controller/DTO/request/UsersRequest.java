package com.eum.auth.controller.DTO.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class UsersRequest {
    @Getter
    @Setter
    public static class Token{
        @NotEmpty
        private String token;
    }
    //자체 로그인 폼
    @Getter
    @Setter
    public static class SignIn{
        @NotEmpty(message = "이메일을 입력하세요")
//        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String testmail;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(testmail, password);
        }
    }

    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }
    @Getter
    @Setter
    public static class Logout {

        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;

    }
    @Getter
    @Setter
    public static class Withdrawal {
        @Positive(message = "탈퇴 사유 카테고리 id는 양수입니다")
        private Long categoryId;
        private String reason;

    }
    @Getter
    @Setter
    public static class BlockedAction {
        @Positive(message = "유저 아이디는 양수입니다")
        private Long userId;

    }
}
