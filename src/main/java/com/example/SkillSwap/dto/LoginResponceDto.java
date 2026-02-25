package com.example.SkillSwap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponceDto {

    String jwt;
    String username;
}
