package com.eum.auth.client;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "haetsal-service", url = "http://175.45.203.201:8000/haetsal-service/api/v2/profile")
public interface HaetsalClient {
    @PostMapping()
    ProfileResponseDTO.ProfileResponse createProfile(@RequestBody ProfileRequest.CreateProfile createProfile, @RequestHeader("userId") String userId);

    @GetMapping()
    ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponse>> getMyProfile( @RequestHeader("userId") String userId);
}
