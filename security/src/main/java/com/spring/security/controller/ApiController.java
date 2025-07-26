package com.spring.security.controller;

import com.spring.security.dto.UserRequestDTO;
import com.spring.security.dto.UserResponseDTO;
import com.spring.security.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth/security")
@RequiredArgsConstructor
public class ApiController {
    private static final Logger log = LogManager.getLogger(ApiController.class);

    private final AuthService authService;



    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok("Working");
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRequestDTO request, @RequestParam List<String> roles) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO request) {
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        UserResponseDTO responseDTO =new UserResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setLastLogin(LocalDateTime.now());
        responseDTO.setUserName(request.getUsername());
        return ResponseEntity.ok(responseDTO);
    }
}
