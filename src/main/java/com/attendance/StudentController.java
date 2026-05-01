package com.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    // In-memory list to hold students for now
    private final List<Student> students = new ArrayList<>();

    // GET /api/students -> Returns all students
    @GetMapping
    public List<Student> getAllStudents() {
        return students;
    }

    // POST /api/students -> Adds a new student
    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        students.add(student);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Student added successfully");
        return ResponseEntity.ok(response);
    }
}