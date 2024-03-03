package com.eum.auth.controller.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


public class ProfileRequest {
    @Getter
    @Setter
    public static class CreateProfile{
        private String nickname;


    }


}
