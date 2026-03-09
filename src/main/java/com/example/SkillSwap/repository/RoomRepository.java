package com.example.SkillSwap.repository;

import com.example.SkillSwap.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long>  {

    Optional<Room> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    Optional<Room> findByRoomId(String roomId);
}
