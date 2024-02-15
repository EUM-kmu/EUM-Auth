package com.eum.auth.service;

import com.eum.auth.client.HaetsalServiceClient;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final HaetsalServiceClient haetsalServiceClient;
    public ProfileResponseDTO.ProfileResponse create(ProfileRequest.CreateProfile createProfile, MultipartFile multipartFile) {


    }
}
