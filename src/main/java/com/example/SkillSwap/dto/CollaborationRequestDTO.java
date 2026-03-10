package com.example.SkillSwap.dto;

import com.example.SkillSwap.type.RequestStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CollaborationRequestDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private String senderImage;
    private RequestStatus status;
    private LocalDateTime createdAt;
}