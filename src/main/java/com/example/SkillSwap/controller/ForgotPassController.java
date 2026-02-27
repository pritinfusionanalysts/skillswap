package com.example.SkillSwap.controller;

import com.example.SkillSwap.dto.OtpDto;
import com.example.SkillSwap.dto.PassForgotDto;
import com.example.SkillSwap.dto.ResetPasswordDto;
import com.example.SkillSwap.service.UserLoginSignupService;
import com.example.SkillSwap.service.UserPasswordForgotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/forgot")
@RequiredArgsConstructor
public class ForgotPassController {

    private final UserPasswordForgotService userPasswordForgotService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> userCheck(@RequestBody PassForgotDto passForgotDto){
        System.out.println(passForgotDto.getEmail());

        boolean valid = userPasswordForgotService.uerVarification(passForgotDto);

        Map<String, String> response = new HashMap<>();

        if(valid){

            response.put("message", "A password reset link has been sent to your email.");
            return  ResponseEntity.ok(response);
        }
        else{
            response.put("message", "user not found ");
            return ResponseEntity.status(404).body(response);
        }


    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpDto dto) {

        boolean valid = userPasswordForgotService.verifyUserOtp(dto.getEmail(), dto.getOtp());

        Map<String, String> response = new HashMap<>();

        if (valid) {
            response.put("message", "OTP verified successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid or expired OTP");
            return ResponseEntity.status(400).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {

        boolean success = userPasswordForgotService.resetPassword(
                dto.getEmail(),
                dto.getNewPassword()
        );

        Map<String, String> response = new HashMap<>();

        if (success) {
            response.put("message", "Password updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "OTP not verified or user not found");
            return ResponseEntity.status(400).body(response);
        }
    }


}
