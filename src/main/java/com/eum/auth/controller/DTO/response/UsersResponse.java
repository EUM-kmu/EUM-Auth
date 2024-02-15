package com.eum.auth.controller.DTO.response;

import com.eum.auth.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UsersResponse {
    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
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
