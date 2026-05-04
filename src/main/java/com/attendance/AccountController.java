package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class AccountController {

    private final List<AdminAccount> accounts = new ArrayList<>();
    private final String ADMIN_KEY = "SupportAdmin@2026";

    public AccountController() {
        // ISSUE 3 FIX: Always ensure the master account exists even if server restarts
        accounts.add(new AdminAccount("MainHeadAcc", "SupportHead@2026")); 
    }

    @GetMapping("/accounts")
    public List<String> getAccountUsernames() {
        List<String> usernames = new ArrayList<>();
        for (AdminAccount acc : accounts) {
            usernames.add(acc.getUsername());
        }
        return usernames;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        for (AdminAccount acc : accounts) {
            if (acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                return ResponseEntity.ok(Map.of("success", true));
            }
        }
        return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid credentials"));
    }

    @PostMapping("/add-account")
    public ResponseEntity<Map<String, Object>> addAccount(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
            @RequestBody AdminAccount newAccount) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }

        for (AdminAccount acc : accounts) {
            if (acc.getUsername().equals(newAccount.getUsername())) {
                 return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username already exists"));
            }
        }
        
        accounts.add(newAccount);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/delete-account/{username}")
    public ResponseEntity<Map<String, Object>> deleteAccount(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey,
            @PathVariable String username) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }

        if ("MainHeadAcc".equals(username)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cannot delete default admin"));
        }

        accounts.removeIf(acc -> acc.getUsername().equals(username));
        return ResponseEntity.ok(Map.of("success", true));
    }
}
