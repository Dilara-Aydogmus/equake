package com.dilara.equake.controller;

import com.dilara.equake.model.AnomalyData;
import com.dilara.equake.model.UserInfo;
import com.dilara.equake.service.AnomalyService;
import com.dilara.equake.service.UserInfoService;
import com.dilara.equake.service.AdviceService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class VibrationController {

    @Autowired
    private AnomalyService anomalyService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AdviceService adviceService;

    @PostMapping("/api/vibration/analyze")
    public String analyzeData(Model model) {
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
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    AnomalyData data = new AnomalyData();
                    data.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), formatter));
                    data.setVibrationX(json.getDouble("vibration_x"));
                    data.setVibrationY(json.getDouble("vibration_y"));
                    data.setVibrationZ(json.getDouble("vibration_z"));
                    data.setScore(json.getDouble("score"));
                    data.setAnomaly(json.getBoolean("anomaly"));

                    anomalyService.save(data);
                }
            }

            process.waitFor();

            // Kullanıcı bilgilerini al
            UserInfo user = userInfoService.findLatest();

            // Tavsiye al
            String advice = adviceService.getPersonalizedAdvice(user.getAge(), user.getLocation(), user.getFloorType());

            // Thymeleaf'e gönder
            model.addAttribute("advice", advice);

        } catch (Exception e) {
            model.addAttribute("advice", "Bir hata oluştu: " + e.getMessage());
        }

        return "analyze"; // templates/analyze.html
    }
}
