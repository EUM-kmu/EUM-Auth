package com.eum.auth.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByUidAndRole(String uid, Role role);

    Boolean existsByRole(Role role);
    Optional<User> findByEmail(String username);
    Optional<User> findByUid(String uid);

    Boolean existsByUid(String uid);
}
