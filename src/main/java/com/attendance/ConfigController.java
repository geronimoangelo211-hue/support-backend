package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
// STRICT CORS: Only your Vercel websites are allowed to talk to this API now!
@CrossOrigin(origins = {"https://os-register.vercel.app", "https://YOUR-MAIN-DASHBOARD-URL.vercel.app"}) 
public class ConfigController {

    public static boolean isSystemLocked = false;
    private final String ADMIN_KEY = "SupportAdmin@2026"; // Secret password for API

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
