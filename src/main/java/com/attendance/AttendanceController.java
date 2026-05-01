package com.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class AttendanceController {

    // In-memory storage for logs
    private final List<AttendanceLog> logs = new ArrayList<>();

    // Get all logs
    @GetMapping
    public List<AttendanceLog> getAllLogs() {
        return logs;
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncLogs(@RequestBody List<AttendanceLog> newLogs) {
        logs.clear();
        logs.addAll(newLogs);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // Clear all logs (For the Developer "Clear All Logs" button)
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearLogs() {
        logs.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "All logs permanently deleted");
        return ResponseEntity.ok(response);
    }
}
