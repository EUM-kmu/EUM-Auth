package com.eum.auth.controller.DTO.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ProfileResponseDTO {

    @Getter
    @Setter
    @Builder
    @Schema(description = "프로필 정보")
    public static class ProfileResponse {
        private Long profileId;

        @Schema(description = "닉네임")
        private String nickName;
        @Schema(description = "성별",allowableValues = {"male","female"})
        private String gender;
        private String address;
        @Schema(description = "나이대",example = "20")
        private int ageRange;
        @Schema(description = "계좌정보")
        private String accountNumber;
        @Schema(description = "이미지 url")
        private String profileImage; //네이버 클라우드 Url
        @Schema(description = "치단 여부")
        private Boolean blocked;

    }
    @Getter
    @Setter
    @Builder
    public static class ProfileResponseWithToken {
        private ProfileResponse profile;
        private UserResponse.TokenInfo tokenInfo;
    }






    }
