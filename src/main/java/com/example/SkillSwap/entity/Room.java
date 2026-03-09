package com.example.SkillSwap.entity;


import jakarta.persistence.*;
import lombok.*;


@Data
@RequiredArgsConstructor
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"user1Id", "user2Id"}))
@Builder
@AllArgsConstructor
@Entity(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user1Id;

    private Long user2Id;

    private String roomId;

}
