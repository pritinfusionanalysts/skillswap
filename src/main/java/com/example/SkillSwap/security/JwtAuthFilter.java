package com.example.SkillSwap.security;


import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // No token public / login request
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token safely
        String token = authHeader.substring(7); // remove "Bearer "

        // Invalid token  skip
        if (!authUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract username
        String username = authUtil.getUsernameFromToken(token);

        Optional<User> userOptional = userRepository.findByUsername(username);

        // User not found skip
        if (userOptional.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        User user =userOptional.get();

        // Set authentication (IMPORTANT)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );


        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

