package com.eum.auth.domain.user;

import com.eum.auth.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String uid;
    private String email;
    private String password;
    private Boolean isBanned;
    private boolean isDeleted;

    public void setDeleted() {
        isDeleted = true;
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


}
