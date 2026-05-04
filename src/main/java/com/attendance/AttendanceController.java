package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/history/{date}")
    public ResponseEntity<List<AttendanceLog>> getHistoryForDate(@PathVariable String date) {
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
        
        // Automatically clear tombstones when real attendance is recorded
        logs.removeIf(l -> "SYS_WIPE_ALL".equals(l.getId()) || "SYS_WIPE_LOGS".equals(l.getId()));

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

        logs.clear();
        if (newLogs != null) {
            logs.addAll(newLogs);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    // NEW: Guaranteed Server-Side Logs Wipe
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }
        
        logs.clear();
        
        AttendanceLog tombstone = new AttendanceLog();
        tombstone.setId("SYS_WIPE_LOGS");
        tombstone.setAction("WIPED");
        logs.add(tombstone);
        
        return ResponseEntity.ok(Map.of("success", true));
    }

    // NEW: Guaranteed Server-Side Factory Reset Wipe
    @DeleteMapping("/factory-reset")
    public ResponseEntity<Map<String, Object>> factoryReset(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }
        
        logs.clear();
        
        AttendanceLog tombstone = new AttendanceLog();
        tombstone.setId("SYS_WIPE_ALL");
        tombstone.setAction("WIPED");
        logs.add(tombstone);
        
        return ResponseEntity.ok(Map.of("success", true));
    }
}
