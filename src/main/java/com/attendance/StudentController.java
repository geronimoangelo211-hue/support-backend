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
    private final String ADMIN_KEY = "SupportAdmin@2026";

    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
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
        
        // HACKER CHECK
        if (!ADMIN_KEY.equals(adminKey)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "UNAUTHORIZED HACKER"));
        }

        students.clear();
        if (newStudents != null) {
            students.addAll(newStudents);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
