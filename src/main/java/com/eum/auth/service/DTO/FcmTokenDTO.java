package com.eum.auth.service.DTO;

import lombok.Data;

@Data
public class FcmTokenDTO {
    private Long userId;
//    private String token;

    public FcmTokenDTO(Long userId) {
        this.userId = userId;
//        this.token = fcmToken.getToken();
    }
}
