package com.eum.auth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthService {
    public void createAccountInFirebase(String email,String uid) throws FirebaseAuthException{

        CreateRequest request = new CreateRequest()
                .setEmail(email)
                .setEmailVerified(false)
                .setPassword(uid)
                .setDisabled(false);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        log.info(userRecord.getUid());


    }
    public Boolean checkAuth(String uid)  {
        try {
            FirebaseAuth.getInstance().getUserByEmail(uid);
            return true;
        }catch (FirebaseAuthException e) {
            return false;
        }
    }
}
