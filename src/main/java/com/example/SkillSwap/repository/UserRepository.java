package com.example.SkillSwap.repository;

import com.example.SkillSwap.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SkillSwap.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByProviderIdAndAuthProviderType(String providerId, AuthProviderType providerType);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);


    boolean existsByUsername(String username);
}
