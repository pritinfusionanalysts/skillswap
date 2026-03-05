package com.example.SkillSwap.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateDTO {
    private String username;
    private String bio;
    private String location;
    private String availability;
    private String preferredMode;
    private String gender;
    private MultipartFile image;
}
