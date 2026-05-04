package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*") 
public class AttendanceController {

    private final List<AttendanceLog> logs = new ArrayList<>();
    private final String ADMIN_KEY = "SupportAdmin@2026";

    // Gets ALL logs
    @GetMapping
    public List<AttendanceLog> getAllLogs() {
        return logs;
    }

    // ISSUE 1 & 2 FIX: Securely get history for a specific date, including No Attendance
    @GetMapping("/history/{date}")
    public ResponseEntity<List<AttendanceLog>> getHistoryForDate(@PathVariable String date) {
        // Decode URL encoded slashes (e.g. 5%2F4%2F2026 -> 5/4/2026)
        String decodedDate = date.replace("%2F", "/");
        
        List<AttendanceLog> dailyLogs = logs.stream()
            .filter(log -> decodedDate.equals(log.getDate()))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dailyLogs);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addLog(@RequestBody AttendanceLog log) {
        if (ConfigController.isSystemLocked) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "SYSTEM_LOCKED"));
        }
        logs.add(log);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody List<AttendanceLog> newLogs) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }
        if (ConfigController.isSystemLocked) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "SYSTEM_LOCKED"));
        }

        // ISSUE 1 FIX: Don't clear logs blindly. Only clear if it's a global wipe.
        if (newLogs != null && !newLogs.isEmpty() && "SYS_WIPE_LOGS".equals(newLogs.get(0).getId())) {
             logs.clear();
        } else if (newLogs != null) {
            logs.clear();
            logs.addAll(newLogs);
        }
        
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }
        logs.clear();
        return ResponseEntity.ok(Map.of("success", true));
    }
}
