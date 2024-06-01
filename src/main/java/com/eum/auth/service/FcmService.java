package com.eum.auth.service;

import com.eum.auth.domain.user.User;
import com.eum.auth.messageq.FcmProducer;
import com.eum.auth.service.DTO.FcmTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final FcmProducer fcmProducer;
    private final UsersService usersService;

    public void deleteFcmToken( Long userId){
        User user = usersService.findById(userId);

        FcmTokenDTO fcmTokenDTO = new FcmTokenDTO(user.getUserId());
        fcmProducer.send(fcmTokenDTO);

    }


}
