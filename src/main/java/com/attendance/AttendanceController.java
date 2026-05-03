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

    // Open so Dashboard can read history
    @GetMapping
    public List<AttendanceLog> getAllLogs() {
        return logs;
    }

    // Open so students can append a single log safely
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

    // DANGEROUS OVERWRITE ZONE: Strictly locked down
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody List<AttendanceLog> newLogs) {
        
        // IRONCLAD HACKER CHECK
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED ACCESS: Cannot overwrite logs without Admin Key"));
        }

        if (ConfigController.isSystemLocked) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "SYSTEM_LOCKED"));
        }

        logs.clear();
        if (newLogs != null) {
            logs.addAll(newLogs);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // DANGEROUS WIPE ZONE: Strictly locked down
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        // IRONCLAD HACKER CHECK
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED ACCESS: Cannot delete logs without Admin Key"));
        }

        logs.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
