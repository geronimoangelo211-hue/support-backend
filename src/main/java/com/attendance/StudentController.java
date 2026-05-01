package com.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // <--- This allows Vercel to talk to this specific controller!
public class StudentController {

    // In-memory list to hold students
    private final List<Student> students = new ArrayList<>();

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // Add a single student
    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        students.add(student);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student added successfully");
        return ResponseEntity.ok(response);
    }

    // SYNC ENDPOINT: Overwrites the cloud with the latest data from any device
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncStudents(@RequestBody List<Student> newStudents) {
        students.clear();
        if (newStudents != null) {
            students.addAll(newStudents);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
