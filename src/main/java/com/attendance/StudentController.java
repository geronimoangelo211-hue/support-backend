package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") 
public class StudentController {

    private final List<Student> students = new ArrayList<>();
    private final String ADMIN_KEY = "HeadOnlineSupport@333";

    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        // Clear the tombstone automatically when a new student is added
        students.removeIf(s -> "SYS_WIPE_ALL".equals(s.getId()));

        students.add(student);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student added successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncStudents(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey, 
            @RequestBody List<Student> newStudents) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }

        students.clear();
        if (newStudents != null) {
            students.addAll(newStudents);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // NEW: Guaranteed Server-Side Factory Reset
    @DeleteMapping("/factory-reset")
    public ResponseEntity<Map<String, Object>> factoryReset(
            @RequestHeader(value = "X-Admin-Key", required = false) String adminKey) {
        
        if (adminKey == null || !ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED"));
        }
        
        // 1. Physically destroy all student data
        students.clear();
        
        // 2. Create the Tombstone directly in Java so it never fails JSON parsing
        Student tombstone = new Student();
        tombstone.setId("SYS_WIPE_ALL");
        tombstone.setName("WIPED");
        students.add(tombstone);
        
        return ResponseEntity.ok(Map.of("success", true));
    }
}
