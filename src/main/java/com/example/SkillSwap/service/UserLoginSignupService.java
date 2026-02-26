package com.example.SkillSwap.service;

import com.example.SkillSwap.dto.LoginRequestDto;
import com.example.SkillSwap.dto.LoginResponceDto;
import com.example.SkillSwap.repository.UserRepository;
import com.example.SkillSwap.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.example.SkillSwap.entity.User;


@Service
@RequiredArgsConstructor
public class UserLoginSignupService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    public LoginResponceDto userValidate(LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = authUtil.genrateAccessTocken(user);

        return new LoginResponceDto(token, user.getUsername());

    }
}
