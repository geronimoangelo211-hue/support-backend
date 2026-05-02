package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*") // <--- Allows Vercel to sync logs!
public class AttendanceController {

    private final List<AttendanceLog> logs = new ArrayList<>();

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

    // SYNC ENDPOINT: Overwrites the cloud logs
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncLogs(@RequestBody List<AttendanceLog> newLogs) {
        logs.clear();
        if (newLogs != null) {
            logs.addAll(newLogs);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs() {
        logs.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
