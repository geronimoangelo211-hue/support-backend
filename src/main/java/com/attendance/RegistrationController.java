package com.attendance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    // This holds the one single active token. 
    // When a new one is generated, the old one is overwritten and destroyed!
    private String activeToken = null;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateToken() {
        activeToken = UUID.randomUUID().toString();
        Map<String, String> response = new HashMap<>();
        response.put("token", activeToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", token != null && token.equals(activeToken));
        return ResponseEntity.ok(response);
    }
}