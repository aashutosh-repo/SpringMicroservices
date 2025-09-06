package com.spring.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class UserRequestDTO {
    private String username;
    private  String password;
}
