package com.dilara.equake.controller;

import com.dilara.equake.model.AnomalyData;
import com.dilara.equake.repository.AnomalyDataRepository;
import com.dilara.equake.service.AnomalyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/vibration")
public class VibrationController {

    @Autowired
    private AnomalyService anomalyService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeData() {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder pb = new ProcessBuilder("python", "src/main/python/analyze_vibration.py");
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");

                if (line.startsWith("{")) {
                    JSONObject json = new JSONObject(line);

                    AnomalyData data = new AnomalyData();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    data.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), formatter));

                    data.setVibrationX(json.getDouble("vibration_x"));
                    data.setVibrationY(json.getDouble("vibration_y"));
                    data.setVibrationZ(json.getDouble("vibration_z"));
                    data.setScore(json.getDouble("score"));
                    data.setAnomaly(json.getBoolean("anomaly"));

                    anomalyService.save(data);
                }
            }

            int exitCode = process.waitFor();
            output.append("Process exited with code ").append(exitCode);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Hata oluştu: " + e.getMessage());
        }

        return ResponseEntity.ok("Python çıktı:\n" + output);
    }
}
