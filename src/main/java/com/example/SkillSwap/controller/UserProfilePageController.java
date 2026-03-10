package com.example.SkillSwap.controller;


import com.example.SkillSwap.dto.UserProfileUpdateDTO;
import com.example.SkillSwap.dto.UserResponseDTO;
import com.example.SkillSwap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.SkillSwap.entity.User;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserProfilePageController {

    private final UserService userService;


    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @ModelAttribute UserProfileUpdateDTO updateDto,
            @AuthenticationPrincipal UserDetails currentUser) {

        try {
            // Pass the username from the JWT and the DTO to the service
            User updatedUser = userService.updateUserDetail(currentUser.getUsername(), updateDto);

            System.out.println(updateDto.getAvailability());

            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating profile: " + e.getMessage());
        }
    }


    @GetMapping("/profile-data")
    public ResponseEntity<UserResponseDTO> userProfileData(@AuthenticationPrincipal UserDetails currentUser) {
        // Pass the username from the SecurityContext to the service
        UserResponseDTO profileData = userService.getUserProfileData(currentUser.getUsername());

        return ResponseEntity.ok(profileData);
    }


}
