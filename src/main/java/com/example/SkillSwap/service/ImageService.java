package com.example.SkillSwap.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    private final String uploadDir = "uploads/profiles/";


    public String saveToDisk(MultipartFile file) throws IOException {
        // 1. Create the path object
        Path uploadPath = Paths.get(uploadDir);

        // 2. Create the directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Generate a Unique Filename (e.g., a1b2-c3d4_myphoto.jpg)
        // This prevents errors if two users upload "profile.jpg"
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 4. Resolve the full path and save the file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 5. Return the relative path to be saved in the Database
        // Your Frontend will use this to show the image
        return "/uploads/profiles/" + fileName;
    }
}
