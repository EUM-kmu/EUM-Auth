package com.eum.auth.controller;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.common.DTO.enums.SuccessCode;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import com.eum.auth.domain.CustomUserDetails;
import com.eum.auth.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth-service/profile/api/v2")
@RestController
@CrossOrigin("*")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping()
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> createProfile(@RequestPart(value = "request") @Validated ProfileRequest.CreateProfile createProfile, @RequestPart(value = "file") MultipartFile multipartFile){
        ProfileResponseDTO.ProfileResponse profileResponse=profileService.create(createProfile,multipartFile);
        return new ResponseEntity<>( APIResponse.of(SuccessCode.INSERT_SUCCESS,profileResponse), HttpStatus.CREATED);
    }

}
