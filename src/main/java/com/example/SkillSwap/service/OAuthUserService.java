package com.example.SkillSwap.service;


import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.repository.UserRepository;
import com.example.SkillSwap.dto.LoginResponceDto;
import com.example.SkillSwap.security.AuthUtil;
import com.example.SkillSwap.type.AuthProviderType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OAuthUserService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Transactional
    public LoginResponceDto handleOAuthLogin(
            OAuth2User oAuth2User,
            String registrationId
    ) {

        AuthProviderType providerType =
                authUtil.getProviderTypeFromRegistrationId(registrationId);

        String providerId =
                authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository
                .findByProviderIdAndAuthProviderType(providerId, providerType)
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername(oAuth2User.getAttribute("email"));
                    u.setProviderId(providerId);
                    u.setAuthProviderType(providerType);
                    u.setRole("user");
                    u.setEnabled(true);
                    return userRepository.save(u);
                });

        String token = authUtil.genrateAccessTocken(user);
        return new LoginResponceDto(token, user.getUsername());
    }
}
