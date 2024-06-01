package com.eum.auth.domain.user;

import com.eum.auth.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String uid;
    private String email;
    private String password;
    private Boolean isBanned;
    private boolean deleted;
    private Long previousUserId;

    public void setDeleted() {
        deleted = true;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;


    public static User toEntity(String uid, SocialType socialType){
        return User.builder().email("")
                .role(Role.ROLE_TEMPORARY_USER)
                .previousUserId(-1L)
                .uid(uid)
                .deleted(false)
                .isBanned(false)
                .socialType(socialType).build();
    }


}
