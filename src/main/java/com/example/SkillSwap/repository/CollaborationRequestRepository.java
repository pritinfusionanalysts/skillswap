package com.example.SkillSwap.repository;

import com.example.SkillSwap.entity.CollaborationRequest;
import com.example.SkillSwap.entity.User;
import com.example.SkillSwap.type.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, Long> {

    // 1. Find a specific request in one direction (A -> B)
    Optional<CollaborationRequest> findBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus status);

    // 2. Used for your /pending list
    List<CollaborationRequest> findByReceiverAndStatus(User receiver, RequestStatus status);

    // 3. THE FIX: Manually define how to find an "existing" request in BOTH directions
    @Query("SELECT r FROM CollaborationRequest r WHERE " +
            "(r.sender = :user1 AND r.receiver = :user2) OR " +
            "(r.sender = :user2 AND r.receiver = :user1)")
    Optional<CollaborationRequest> findExistingRequest(@Param("user1") User user1, @Param("user2") User user2);

    // 4. Alternative for fetching by ID directly
    List<CollaborationRequest> findByReceiverIdAndStatus(Long receiverId, RequestStatus status);

    // Existing helpers
    List<CollaborationRequest> findByReceiver(User receiver);
    List<CollaborationRequest> findBySender(User sender);
}