package com.spring.security.services;

import com.spring.security.configuration.JwtUtil;
import com.spring.security.dto.UserResponseDTO;
import com.spring.security.entity.UserEntity;
import com.spring.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserSessionService sessionService;
    private final JwtUtil jwtUtil;

    public String registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "User registered successfully";
    }

    public UserResponseDTO authenticate(String username, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .toList();
        String token = jwtUtil.generateToken(userDetails.getUsername(), roles);
        UserEntity entity;
        entity= userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->
                new UsernameNotFoundException("User Not Found"));
        sessionService.createSession(entity,token,getClientIp(), getDeviceInfo());

        LocalDateTime expiryTime = sessionService.getExpiryTime(token).plusMinutes(60);

        UserResponseDTO response = new UserResponseDTO();
        response.setToken(token);
        response.setExpiryTime(expiryTime);
        response.setUserName(entity.getUsername());
        response.setLastLogin(LocalDateTime.now());
        return response;
    }

    private String getClientIp() {
        return "127.0.0.1"; // later you can fetch from request
    }

    private String getDeviceInfo() {
        return "Browser"; // or from User-Agent header
    }
}
