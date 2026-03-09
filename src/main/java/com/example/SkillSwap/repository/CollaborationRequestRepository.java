package com.example.SkillSwap.repository;

import com.example.SkillSwap.entity.CollaborationRequest;
import com.example.SkillSwap.type.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.SkillSwap.entity.User;

import java.util.List;
import java.util.Optional;

public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long> {

    Optional<CollaborationRequest>
    findBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus status);

    List<CollaborationRequest> findByReceiver(User receiver);

    List<CollaborationRequest> findBySender(User sender);

    Optional<CollaborationRequest> findExistingRequest(User user1, User user2);
}
