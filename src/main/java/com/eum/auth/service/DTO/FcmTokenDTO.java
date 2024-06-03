package com.eum.auth.service.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class FcmTokenDTO {
    @Getter
    @Setter
    public static class FCMDTO{
        private Long userId;
        private String token;

        public FCMDTO(Long userId, String token) {
            this.userId = userId;
            this.token = token;
        }
    }

    @Getter
    @Setter
    public static class deletedToken{
        private Long userId;

        public deletedToken(Long userId) {
            this.userId = userId;
        }
    }
}
