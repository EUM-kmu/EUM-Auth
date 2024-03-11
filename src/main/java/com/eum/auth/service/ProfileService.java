package com.eum.auth.service;

import com.eum.auth.client.HaetsalClient;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final HaetsalClient haetsalClient;
    public ProfileResponseDTO.ProfileResponse create(ProfileRequest.CreateProfile createProfile,String userId) {

        ProfileResponseDTO.ProfileResponse profileResponse = haetsalClient.createProfile(createProfile,userId);


        return profileResponse;
    }
}
