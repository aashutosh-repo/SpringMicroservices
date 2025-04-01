package com.spring.security.controller;

import com.spring.security.dto.UserRequestDTO;
import com.spring.security.dto.UserResponseDTO;
import com.spring.security.services.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/security")
public class ApiController {
    private static final Logger log = LogManager.getLogger(ApiController.class);

    @Autowired
    private AuthService authService;



    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok("Working");
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRequestDTO request, @RequestParam List<String> roles) {
        return authService.registerUser(request.getUsername(), request.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO request) {
        log.info(request.toString());
        String token = authService.authenticate(request.getUsername(), request.getPassword());

        if (token == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        return ResponseEntity.ok(new UserResponseDTO(token));
    }
}
