package com.spring.core.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/core/secureData")
public class CoreController {

    @PostMapping("/encryption")
    public ResponseEntity<String> encryptData(@RequestParam String requestData){
        return ResponseEntity.ok(requestData);
    }

}
