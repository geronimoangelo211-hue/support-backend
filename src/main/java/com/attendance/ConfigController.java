package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*") // Adjust your CORS as needed
public class ConfigController {

    // FIXED: Changed from private to public so AttendanceController can see it!
    public static boolean isSystemLocked = false;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getSystemStatus() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isLocked", isSystemLocked);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleSystemLock(@RequestBody Map<String, Boolean> request) {
        if (request.containsKey("isLocked")) {
            isSystemLocked = request.get("isLocked");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("isLocked", isSystemLocked);
        return ResponseEntity.ok(response);
    }
}
