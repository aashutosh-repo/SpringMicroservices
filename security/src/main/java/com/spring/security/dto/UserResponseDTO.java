package com.spring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter @AllArgsConstructor
public class UserResponseDTO {
    private String token;
}
