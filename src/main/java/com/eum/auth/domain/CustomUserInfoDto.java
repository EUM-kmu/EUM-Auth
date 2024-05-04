package com.eum.auth.domain;

import com.eum.auth.domain.user.Role;
import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomUserInfoDto {
    Long userId;
    String uid;
    String password;
    boolean isBanned;
    Role role;
    boolean isDeleted;
    Long previousUserId;

}
