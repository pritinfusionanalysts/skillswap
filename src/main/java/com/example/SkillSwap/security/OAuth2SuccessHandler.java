package com.example.SkillSwap.security;


import com.example.SkillSwap.service.OAuthUserService;
import com.example.SkillSwap.dto.LoginResponceDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthUserService oAuthUserService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken token =
                (OAuth2AuthenticationToken) authentication;

        LoginResponceDto loginResponse =
                oAuthUserService.handleOAuthLogin(
                        token.getPrincipal(),
                        token.getAuthorizedClientRegistrationId()
                );

        // Extract JWT
        String jwt = loginResponse.getJwt();

        // Encode token safely for URL
        String encodedToken =
                URLEncoder.encode(jwt, StandardCharsets.UTF_8);

        String username = loginResponse.getUsername();
        // Redirect to React
        response.sendRedirect(
                "http://localhost:3000/oauth2/success?token=" + encodedToken +
                        "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8)
        );
    }
}
