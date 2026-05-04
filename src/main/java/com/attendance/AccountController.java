package com.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest loginRequest) {
        
        AdminAccount account = accountRepository.findByUsername(loginRequest.getUsername());
        
        if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
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

        // Fixed the constructor error by passing all three parameters
        AdminAccount newAccount = new AdminAccount(username, password, role);
        accountRepository.save(newAccount);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account created successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<String>> getAccounts() {
        List<String> usernames = accountRepository.findAll().stream()
                .map(AdminAccount::getUsername)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usernames);
    }

    @DeleteMapping("/delete-account/{user}")
    public ResponseEntity<?> deleteAccount(@PathVariable String user, @RequestHeader(value="X-Admin-Key", required=false) String adminKey) {
        if (!"SupportAdmin@2026".equals(adminKey)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Unauthorized.");
            return ResponseEntity.status(403).body(err);
        }

        if ("MainHeadAcc".equals(user)) {
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("message", "Cannot delete default admin.");
            return ResponseEntity.badRequest().body(err);
        }

        accountRepository.deleteById(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Account deleted.");
        return ResponseEntity.ok(response);
    }
}
