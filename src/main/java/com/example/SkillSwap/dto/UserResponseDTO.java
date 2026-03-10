package com.example.SkillSwap.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder // Makes it easy to create the DTO in the service
public class UserResponseDTO {
    private String username;
    private String email;
    private String bio;
    private String location;
    private String availability;
    private String preferredMode;
    private String gender;
    private String image;
}