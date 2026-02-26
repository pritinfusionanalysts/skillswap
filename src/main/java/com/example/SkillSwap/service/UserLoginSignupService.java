package com.example.SkillSwap.service;

import com.example.SkillSwap.dto.LoginRequestDto;
import com.example.SkillSwap.dto.LoginResponceDto;
import com.example.SkillSwap.dto.SignupRequestDto;
import com.example.SkillSwap.repository.UserRepository;
import com.example.SkillSwap.security.AuthUtil;
import com.example.SkillSwap.type.AuthProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.example.SkillSwap.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class UserLoginSignupService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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


    public User userSave(SignupRequestDto userDTO ) {

        User user = signupInternal(userDTO);
        return user;

    }

    // for internal signup use

    public User signupInternal(SignupRequestDto userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BadCredentialsException("User already exists");
        }
        if(userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required for signup");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole("user");
        user.setEnabled(true);

        user.setAuthProviderType(AuthProviderType.LOCAL);
        user.setProviderId(null);   // IMPORTANT

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);


        return userRepository.save(user);
    }

}
