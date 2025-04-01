package com.spring.customer.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class Controller {

    @PostMapping("/test")
    public ResponseEntity<UserResponseDTO> test(@RequestBody UserRequestDTO user){
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setToken("1234567890ASDFGHJK");
        return ResponseEntity.ok(responseDTO);
    }
}
