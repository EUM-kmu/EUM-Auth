package com.eum.auth.controller;

import com.eum.auth.common.DTO.APIResponse;
import com.eum.auth.common.DTO.enums.SuccessCode;
import com.eum.auth.controller.DTO.request.ProfileRequest;
import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import com.eum.auth.controller.DTO.response.UserResponse;
import com.eum.auth.domain.user.Role;
import com.eum.auth.service.ProfileService;
import com.eum.auth.service.UsersService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("auth-service/api/v2/profile")
@RestController
@CrossOrigin("*")
@Hidden
public class ProfileController {
    private final ProfileService profileService;
    private final UsersService usersService;
    @PostMapping()
    public ResponseEntity<APIResponse<ProfileResponseDTO.ProfileResponseWithToken>> createProfile(@RequestBody  ProfileRequest.CreateProfile createProfile,@RequestHeader("userId") String userId){
        ProfileResponseDTO.ProfileResponse profileResponse=profileService.create(createProfile,userId);
        UserResponse.TokenInfo tokenInfo = usersService.updateRoleAndToken(Role.ROLE_USER, Long.valueOf(userId));
        ProfileResponseDTO.ProfileResponseWithToken response = ProfileResponseDTO.ProfileResponseWithToken.builder().profile(profileResponse).tokenInfo(tokenInfo).build();
        return new ResponseEntity<>( APIResponse.of(SuccessCode.INSERT_SUCCESS,response), HttpStatus.CREATED);
    }

}
