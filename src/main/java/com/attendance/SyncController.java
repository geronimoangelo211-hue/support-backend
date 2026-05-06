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

    @PostMapping("/push")
    public ResponseEntity<?> pushData(@RequestBody Map<String, String> payload) {
        String studentsJson = payload.get("students");
        String logsJson = payload.get("logs");
        String configJson = payload.get("config");

        if (studentsJson != null && !studentsJson.isEmpty()) {
            CloudData d = cloudRepo.findById("STUDENTS").orElse(new CloudData("STUDENTS", ""));
            d.setJsonData(studentsJson);
            cloudRepo.save(d);
        }
        if (logsJson != null && !logsJson.isEmpty()) {
            CloudData d = cloudRepo.findById("LOGS").orElse(new CloudData("LOGS", ""));
            d.setJsonData(logsJson);
            cloudRepo.save(d);
        }
        if (configJson != null && !configJson.isEmpty()) {
            CloudData d = cloudRepo.findById("CONFIG").orElse(new CloudData("CONFIG", ""));
            d.setJsonData(configJson);
            cloudRepo.save(d);
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
        CloudData configData = cloudRepo.findById("CONFIG").orElse(null);

        response.put("students", studentsData != null ? studentsData.getJsonData() : "[]");
        response.put("logs", logsData != null ? logsData.getJsonData() : "[]");
        response.put("config", configData != null ? configData.getJsonData() : "{}");

        return ResponseEntity.ok(response);
    }
}
