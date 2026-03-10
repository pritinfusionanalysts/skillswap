package com.example.SkillSwap.controller;


import com.example.SkillSwap.dto.CollaborationRequestDTO;
import com.example.SkillSwap.entity.CollaborationRequest;
import com.example.SkillSwap.service.UserCollaborationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/collaborations")
public class UserCollaborationController {

    private final UserCollaborationService userCollaborationService;


    @PostMapping("/send/{receiverId}")
    public ResponseEntity<?> sendRequest(
            @PathVariable Long receiverId) {

        return ResponseEntity.ok(
                userCollaborationService.sendRequest(receiverId)
        );
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(
                userCollaborationService.acceptRequest(requestId)
        );
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId) {

        return ResponseEntity.ok(
                userCollaborationService.rejectRequest(requestId)
        );
    }



    @GetMapping("/pending")
    public ResponseEntity<List<CollaborationRequestDTO>> getPending() {
        return ResponseEntity.ok(userCollaborationService.getMyPendingRequests());
    }


}
