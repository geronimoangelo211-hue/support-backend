package com.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*")
public class SyncController {

    @Autowired
    private CloudDataRepository cloudRepo;

    // This receives the data from your Javascript and saves it to the hard drive
    @PostMapping("/push")
    public ResponseEntity<?> pushData(@RequestBody Map<String, String> payload) {
        String studentsJson = payload.get("students");
        String logsJson = payload.get("logs");

        if (studentsJson != null && !studentsJson.isEmpty()) {
            cloudRepo.save(new CloudData("STUDENTS", studentsJson));
        }
        if (logsJson != null && !logsJson.isEmpty()) {
            cloudRepo.save(new CloudData("LOGS", logsJson));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/pull")
    public ResponseEntity<?> pullData() {
        Map<String, String> response = new HashMap<>();
        
        CloudData studentsData = cloudRepo.findById("STUDENTS").orElse(null);
        CloudData logsData = cloudRepo.findById("LOGS").orElse(null);

        response.put("students", studentsData != null ? studentsData.getJsonData() : "[]");
        response.put("logs", logsData != null ? logsData.getJsonData() : "[]");

        return ResponseEntity.ok(response);
    }
}
