package com.example.SkillSwap.controller;


import com.example.SkillSwap.dto.LoginRequestDto;
import com.example.SkillSwap.dto.LoginResponceDto;
import com.example.SkillSwap.service.UserLoginSignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserLoginSignupController {

    private final UserLoginSignupService userLoginSignupService;


    @GetMapping("/testing")
    public String testing(){
        return "never give up , you will win and that is you'r job that you can do it  ";
    }


    // for login
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponceDto response = userLoginSignupService.userValidate(loginRequestDto);

        return ResponseEntity.ok(response);
    }


}
