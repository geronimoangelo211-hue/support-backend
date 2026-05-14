package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*") 
public class ConfigController {

    public static boolean isSystemLocked = false;
    public static long timeOffset = 0; // NEW: Global Time Travel Offset
    public static String dayOverride = ""; // NEW: Global Day Override
    private final String ADMIN_KEY = "HeadOnlineSupport@333";

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("isLocked", isSystemLocked);
        response.put("timeOffset", timeOffset);
        response.put("dayOverride", dayOverride == null ? "" : dayOverride);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleSystemLock(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody Map<String, Boolean> request) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
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

    // NEW: Endpoint to set Global Time Travel
    @PostMapping("/time-travel")
    public ResponseEntity<Map<String, Object>> setTimeTravel(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody Map<String, Object> request) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED HACKER"));
        }

        if (request.containsKey("timeOffset")) {
            timeOffset = ((Number) request.get("timeOffset")).longValue();
        }
        if (request.containsKey("dayOverride")) {
            dayOverride = (String) request.get("dayOverride");
        }
        
        return ResponseEntity.ok(Map.of("success", true));
    }
}
