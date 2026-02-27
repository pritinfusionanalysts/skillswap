package com.example.SkillSwap.repository;

import com.example.SkillSwap.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findTopByEmailOrderByExpiryTimeDesc(String email);
    void deleteByEmail(String email);
}
