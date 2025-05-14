package com.spring.security.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter @Setter
@RequiredArgsConstructor
public class UserResponseDTO {
    private String token;
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDateTime lastLogin;
}
