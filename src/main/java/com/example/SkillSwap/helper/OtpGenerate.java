package com.example.SkillSwap.helper;

import com.example.SkillSwap.entity.Otp;
import com.example.SkillSwap.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OtpGenerate {

    private final OtpRepository otpRepository;
    private static final SecureRandom secureRandom = new SecureRandom(); ;
    private final JavaMailSender mailSender ;


    public  String generateOtp(String email) {
        int otp = 1000 + secureRandom.nextInt(9000);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

         Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(String.valueOf(otp));
        otpEntity.setExpiryTime(expiryTime);
        otpEntity.setVerified(false);

        otpRepository.save(otpEntity);

        return String.valueOf(otp);
    }

    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code - SkillSwap");
        message.setText(
                "Hello,\n\n" +
                        "Your OTP is: " + otp + "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "Do not share this with anyone.\n\n" +
                        "Thanks,\nSkillSwap Team"
        );

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String enteredOtp) {

        Optional<Otp> otpOptional = otpRepository.findTopByEmailOrderByExpiryTimeDesc(email);

        if (otpOptional.isEmpty()) {
            return false;
        }

        Otp otpEntity = otpOptional.get();

        //  Check OTP match
        if (!otpEntity.getOtp().equals(enteredOtp)) {
            return false;
        }

        // Check expiry
        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Mark as verified
        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);

        return true;
    }


}
