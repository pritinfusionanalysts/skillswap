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


    public String sendRequest(Long receiverId) {

        // Get logged-in user
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Prevent self request
        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException("You cannot send request to yourself");
        }

        // Check if already pending
        requestRepository.findBySenderAndReceiverAndStatus(
                sender, receiver, RequestStatus.PENDING
        ).ifPresent(r -> {
            throw new RuntimeException("Request already sent");
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


        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
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

        return "Request Accepted";
    }

    public String rejectRequest(Long requestId) {


        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
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
