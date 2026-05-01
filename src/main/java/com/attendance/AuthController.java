package com.attendance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class AuthController {

    // In-memory storage for multiple accounts
    private final Map<String, String> users = new HashMap<>();

    public AuthController() {
        // Default Admin Account
        users.put("MainHeadAcc", "AccountHead@456");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        System.out.println(">>> LOGIN ATTEMPT RECEIVED FOR: " + request.getUsername());
        
        String storedPassword = users.get(request.getUsername());
        
        if (storedPassword != null && storedPassword.equals(request.getPassword())) {
            System.out.println(">>> LOGIN SUCCESS");
            return ResponseEntity.ok(new LoginResponse(true, "Login successful"));
        } else {
            System.out.println(">>> LOGIN FAILED");
            return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid username or password"));
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<Set<String>> getAccounts() {
        return ResponseEntity.ok(users.keySet());
    }

    @PostMapping("/add-account")
    public ResponseEntity<LoginResponse> addAccount(@RequestBody LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
            request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, "Username and password required."));
        }
        if (users.containsKey(request.getUsername())) {
            return ResponseEntity.badRequest().body(new LoginResponse(false, "Username already exists."));
        }
        
        users.put(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(true, "Account created successfully!"));
    }

    @DeleteMapping("/delete-account/{username}")
    public ResponseEntity<LoginResponse> deleteAccount(@PathVariable String username) {
        // Prevent deleting the new main account
        if ("MainHeadAcc".equals(username)) { 
            return ResponseEntity.badRequest().body(new LoginResponse(false, "Cannot delete the default MainHeadAcc."));
        }
        if (users.remove(username) != null) {
            return ResponseEntity.ok(new LoginResponse(true, "Account deleted."));
        }
        return ResponseEntity.badRequest().body(new LoginResponse(false, "Account not found."));
    }

    // --- DTOs ---
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; } 
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private final boolean success;
        private final String message;

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
