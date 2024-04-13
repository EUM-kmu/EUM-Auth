package com.eum.auth.controller.DTO.response;

import com.eum.auth.domain.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @Schema(description = "토큰 정보")
    public static class TokenInfo {
        @Schema(description = "유저 id")
        private Long userId;
        @Schema(description = "프로필 id")
        private Long profileId;
        @Schema(description = "토큰 타입" ,example ="Bearer" )
        private String grantType;
        @Schema(description = "엑세스 토큰")
        private String accessToken;
        @Schema(description = "리프레시 토큰")
        private String refreshToken;
        @Schema(description = "토큰 만료 시간")
        private Long refreshTokenExpirationTime;
        private Role role;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class UserRole {
        private Role role;

    }



}
