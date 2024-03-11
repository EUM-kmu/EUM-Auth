package com.eum.auth.client;

import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "haetsal-service", url = "http://localhost:80/haetsal-service/api/v2/profile")
public interface HaetsalClient {
    @PostMapping()
    ProfileResponseDTO.ProfileResponse createProfile(@RequestBody ProfileRequest.CreateProfile createProfile, @RequestHeader("userId") String userId);
}
