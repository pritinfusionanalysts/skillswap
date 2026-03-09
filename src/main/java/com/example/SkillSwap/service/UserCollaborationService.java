package com.example.SkillSwap.service;


import com.example.SkillSwap.entity.CollaborationRequest;
import com.example.SkillSwap.repository.CollaborationRequestRepository;
import com.example.SkillSwap.repository.UserRepository;
import com.example.SkillSwap.type.RequestStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.SkillSwap.entity.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCollaborationService {


    private final CollaborationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;


    public String sendRequest(Long receiverId) {

        // Get logged-in user
        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();


        User sender = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Prevent self request
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("You cannot send request to yourself");
        }

        // --- CRITICAL FIX START ---
        // Check if ANY request (Pending OR Accepted) exists in either direction
        requestRepository.findExistingRequest(sender, receiver).ifPresent(r -> {
            if (r.getStatus() == RequestStatus.PENDING) {
                throw new RuntimeException("A pending request already exists between you two.");
            }
            if (r.getStatus() == RequestStatus.ACCEPTED) {
                throw new RuntimeException("You are already collaborators.");
            }
        });

        CollaborationRequest request = new CollaborationRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        requestRepository.save(request);

        return "Collaboration Request Sent Successfully";
    }



    public String acceptRequest(Long requestId) {


        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));


        CollaborationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));


        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to accept this request");
        }


        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        requestRepository.save(request);


        Long senderId = request.getSender().getId();
        Long receiverId = request.getReceiver().getId();


        String roomId = chatRoomService.createChatRoom(senderId, receiverId);


        return roomId;
    }

    public String rejectRequest(Long requestId) {


        String userName = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));


        CollaborationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));


        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to reject this request");
        }


        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request already processed");
        }

        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        return "Request Rejected";
    }
}
