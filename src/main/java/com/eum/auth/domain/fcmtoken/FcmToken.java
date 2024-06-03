package com.eum.auth.domain.fcmtoken;


import com.eum.auth.common.BaseTimeEntity;
import com.eum.auth.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FcmToken extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fcmTokenId;

    @Column
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
