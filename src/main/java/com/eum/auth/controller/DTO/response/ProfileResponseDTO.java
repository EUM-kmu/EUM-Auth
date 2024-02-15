package com.eum.auth.controller.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ProfileResponseDTO {

        @Getter
        @Setter
        @Builder
        public static class ProfileResponse {
            private String nickName;
            private String address;
            private String profileImage; //네이버 클라우드 Url

        }





    }
