package com.example.SkillSwap.service;

import com.example.SkillSwap.entity.Otp;
import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.dto.PassForgotDto;
import com.example.SkillSwap.helper.OtpGenerate;
import com.example.SkillSwap.repository.OtpRepository;
import com.example.SkillSwap.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserPasswordForgotService {

    private final UserRepository userRepository;
    private final OtpGenerate otpGenerate;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean uerVarification(PassForgotDto passForgotDto){
        Optional<User> userOpt = userRepository.findByEmail(passForgotDto.getEmail());



        if (userOpt.isPresent()) {
            try {

                String otp = otpGenerate.generateOtp(passForgotDto.getEmail());
                otpGenerate.sendOtpEmail(passForgotDto.getEmail(), otp);
                return true;

            } catch (Exception e) {

                System.err.println("CRITICAL: Failed to send OTP email: " + e.getMessage());
                return false;
            }
        }

        return false;
    }


    public boolean verifyUserOtp(String email, String otp) {
        return otpGenerate.verifyOtp(email, otp);
    }

    public boolean resetPassword(String email, String newPassword) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }

        //  Check if OTP is verified
        Optional<Otp> otpOpt = otpRepository.findTopByEmailOrderByExpiryTimeDesc(email);

        if (otpOpt.isEmpty() || !otpOpt.get().isVerified()) {
            return false;
        }

        User user = userOpt.get();

        //  Encrypt password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete OTP after success
        otpRepository.deleteByEmail(email);

        return true;
    }

}
