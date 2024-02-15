package com.eum.auth.client;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "haetsal-service", url = "http://localhost:80/api/v1/")
public interface HaetsalServiceClient {
    @PostMapping("/profile")
    ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> createProfile(@RequestPart(value = "request") @Validated ProfileRequest.CreateProfile createProfile, @RequestPart(value = "file") MultipartFile multipartFile);
}
