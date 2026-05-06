package com.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void initDefaultAccounts() {
        if (!accountRepository.existsById("DEVELOPER")) {
            accountRepository.save(new AdminAccount("DEVELOPER", "markangelo@321", "ADMIN"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        
        AdminAccount account = accountRepository.findByUsername(loginRequest.getUsername());
        
        if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
            
            account.setLastOnline(System.currentTimeMillis());
            accountRepository.save(account);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("role", account.getRole()); 
            return ResponseEntity.ok(response);
        }
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Invalid credentials.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // NEW: Heartbeat endpoint to keep the "Last Online" stamp fresh while active
    @PostMapping("/heartbeat/{username}")
    public ResponseEntity<?> heartbeat(@PathVariable String username) {
        AdminAccount account = accountRepository.findByUsername(username);
        if (account != null) {
            account.setLastOnline(System.currentTimeMillis());
            accountRepository.save(account);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/add-account")
    public ResponseEntity<?> addAccount(@RequestBody Map<String, String> payload, @RequestHeader(value="X-Admin-Key", required=false) String adminKey) {
        if (!"SupportAdmin@2026".equals(adminKey)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Unauthorized.");
            return ResponseEntity.status(403).body(err);
        }

        String username = payload.get("username");
        String password = payload.get("password");
        String role = payload.get("role");

        if (username == null || password == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Missing fields.");
            return ResponseEntity.badRequest().body(err);
        }

        if (accountRepository.existsById(username)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Username already exists.");
            return ResponseEntity.badRequest().body(err);
        }

        if (role == null || role.isEmpty()) {
            role = "ADMIN";
        }

        AdminAccount newAccount = new AdminAccount(username, password, role);
        accountRepository.save(newAccount);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account created successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Map<String, Object>>> getAccounts() {
        List<Map<String, Object>> accounts = accountRepository.findAll().stream()
                .map(acc -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", acc.getUsername());
                    map.put("role", acc.getRole() != null ? acc.getRole() : "ADMIN");
                    map.put("lastOnline", acc.getLastOnline());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @DeleteMapping("/delete-account/{user}")
    public ResponseEntity<?> deleteAccount(@PathVariable String user, @RequestHeader(value="X-Admin-Key", required=false) String adminKey) {
        if (!"SupportAdmin@2026".equals(adminKey)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Unauthorized.");
            return ResponseEntity.status(403).body(err);
        }

        if ("DEVELOPER".equals(user)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Cannot delete the developer admin account.");
            return ResponseEntity.badRequest().body(err);
        }

        accountRepository.deleteById(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account deleted.");
        return ResponseEntity.ok(response);
    }
}
