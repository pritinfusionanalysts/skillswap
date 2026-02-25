package com.example.SkillSwap.entity;


import com.example.SkillSwap.type.AuthProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
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
    private String image;

    @Column
    private String bio;


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
    private List<UserSkill> userSkills;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<CollaborationRequest> sentRequests;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<CollaborationRequest> receivedRequests;

}