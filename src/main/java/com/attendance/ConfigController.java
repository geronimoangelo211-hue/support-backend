package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*") // Reverted to allow connection. Hackers are stopped by the Secret Key!
public class ConfigController {

    public static boolean isSystemLocked = false;
    private final String ADMIN_KEY = "SupportAdmin@2026";

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getSystemStatus() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLocked", isSystemLocked);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleSystemLock(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody Map<String, Boolean> request) {
        
        // HACKER CHECK
        if (!ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED HACKER"));
        }

        if (request.containsKey("isLocked")) {
            isSystemLocked = request.get("isLocked");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("isLocked", isSystemLocked);
        return ResponseEntity.ok(response);
    }
}
