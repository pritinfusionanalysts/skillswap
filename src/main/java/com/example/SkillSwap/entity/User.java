package com.example.SkillSwap.entity;


import com.example.SkillSwap.type.AuthProviderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.SkillSwap.type.Mode;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false , unique = true)
    private String email;


    @Column(name="password", nullable = true)
    private String password;

    @Column
    private String gender;

    @Column
    private String image;

    @Column
    private String bio;

    @Column
    private String location;


    @Column
    private String availability;    // Example: Weekdays 7-9 PM

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mode preferredMode = Mode.ONLINE;   // Online / Offline / Hybrid


    @Column(nullable = false)
    private String role;   // ADMIN, USER

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = true)
    private String providerId;


    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;


    public User() {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserSkill> userSkills;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CollaborationRequest> sentRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CollaborationRequest> receivedRequests;


}