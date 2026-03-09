package com.example.SkillSwap.service;

import com.example.SkillSwap.entity.Room;
import com.example.SkillSwap.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository chatRoomRepository;

    public String createChatRoom(Long user1, Long user2) {

        Long firstUser = Math.min(user1, user2);
        Long secondUser = Math.max(user1, user2);

        String roomId = "room_" + firstUser + "_" + secondUser;

        // check if room already exists
        Optional<Room> existingRoom =
                chatRoomRepository.findByRoomId(roomId);

        if (existingRoom.isPresent()) {
            return existingRoom.get().getRoomId();
        }

        Room chatRoom = Room.builder()
                .user1Id(user1)
                .user2Id(user2)
                .roomId(roomId)
                .build();

        chatRoomRepository.save(chatRoom);

        return roomId;
    }
}
