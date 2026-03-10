package com.example.SkillSwap.service;

import com.example.SkillSwap.dto.UserProfileUpdateDTO;
import com.example.SkillSwap.dto.UserResponseDTO;
import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.repository.UserRepository;
import com.example.SkillSwap.type.Mode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserService
{

    private  final UserRepository userRepository;
    private final ImageService imageService;

    public User updateUserDetail(String username, UserProfileUpdateDTO updateDto) throws IOException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not Found"));


        if(updateDto.getBio() != null){
            user.setBio(updateDto.getBio());
        }

        if(updateDto.getAvailability() != null){
            user.setAvailability(updateDto.getAvailability());
        }

        if(updateDto.getLocation() != null){
            user.setLocation(updateDto.getLocation());
        }

        if(updateDto.getPreferredMode() != null && !updateDto.getPreferredMode().isEmpty()){
            user.setPreferredMode(Mode.valueOf(updateDto.getPreferredMode()));
        }

        if(updateDto.getGender() != null){
            user.setGender(updateDto.getGender());
        }

        if (updateDto.getImage() != null && !updateDto.getImage().isEmpty()) {
            String imagePath = imageService.saveToDisk(updateDto.getImage());
            user.setImage(imagePath);
        }

        return  userRepository.save(user);
    }


    public UserResponseDTO getUserProfileData(String username) {
        // 1. Find the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Map the Entity data to the DTO
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .location(user.getLocation())
                .availability(user.getAvailability())
                .preferredMode(user.getPreferredMode() != null
                        ? user.getPreferredMode().toString()
                        : "ONLINE")
                .gender(user.getGender())
                .image(user.getImage())
                .build();
    }

}
