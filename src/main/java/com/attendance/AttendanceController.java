package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*") 
public class AttendanceController {

    private final List<AttendanceLog> logs = new ArrayList<>();
    private final String ADMIN_KEY = "SupportAdmin@2026";

    @GetMapping
    public List<AttendanceLog> getAllLogs() {
        return logs;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addLog(@RequestBody AttendanceLog log) {
        if (ConfigController.isSystemLocked) {
            return ResponseEntity.status(403).body(Map.of(
                "success", false, 
                "message", "SYSTEM_LOCKED"
            ));
        }
        logs.add(log);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody List<AttendanceLog> newLogs) {
        
        // HACKER CHECK
        if (!ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED HACKER"));
        }

        if (ConfigController.isSystemLocked) {
            return ResponseEntity.status(403).body(Map.of(
                "success", false, 
                "message", "SYSTEM_LOCKED"
            ));
        }

        logs.clear();
        if (newLogs != null) {
            logs.addAll(newLogs);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // HACKER CHECK
        if (!ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED HACKER"));
        }

        logs.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
