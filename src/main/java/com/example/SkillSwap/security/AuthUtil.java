package com.example.SkillSwap.security;


import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.type.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class AuthUtil {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String genrateAccessTocken(User user) {

        return Jwts.builder().setSubject(user.getUsername()).claim("userid", user.getId())// who the token belongs to
                .setIssuedAt(new Date())         // token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .signWith(getSecretKey()).compact();

    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token); // verifies signature + expiry
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }



    // for fetching  OAuth provider type
    public AuthProviderType getProviderTypeFromRegistrationId(String registrationid){
        return  switch(registrationid.toLowerCase()){
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            case "facebook" -> AuthProviderType.FACEBOOK;
            default -> throw new IllegalArgumentException("unsupported OAuth2 provider "+registrationid);
        };

    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User,String registrationId){

        String providerId =   switch(registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();

            default -> {
                log.error("unsupported OAuth2 provider "+registrationId);
                throw new IllegalArgumentException("unsupported OAuth2 provider "+registrationId);
            }
        };

        if(providerId == null || providerId.isBlank()){
            log.error("unable to determine providerid for provider "+registrationId);
            throw new IllegalArgumentException("unable to determine providerid for provider for OAuth2 login");
        }

        return providerId;
    }


    public String detemineUsernameFromOAuth2User(OAuth2User oAuth2User,String registrationId,String providerId){
        String email = oAuth2User.getAttribute("emial");

        if(email != null && !email.isBlank()){
            return email;
        }

        return switch(registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;

        };
    }

}
