package com.dilara.equake.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class AdviceService {

    public String getPersonalizedAdvice(int age, String location, String floorType) {
        try {
            // Python dosyasının yolu
            String scriptPath = "src/main/python/advice_generator.py";

            // ProcessBuilder ile çalıştır
            ProcessBuilder pb = new ProcessBuilder(
                    "python", scriptPath,
                    String.valueOf(age), location, floorType
            );
            pb.redirectErrorStream(true); // stderr + stdout birleştir

            Process process = pb.start();

            // Çıktıyı oku
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            process.waitFor(); // Python kodu bitene kadar bekle
            return output.toString(); // JSON string olarak döner

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Tavsiyeye erişilemedi\"}";
        }
    }

    public String extractAdviceOnly(int age, String location, String floorType) {
        try {
            String jsonResponse = getPersonalizedAdvice(age, location, floorType);
            JSONObject json = new JSONObject(jsonResponse);
            return json.optString("advice", "Tavsiye bulunamadı.");
        } catch (Exception e) {
            e.printStackTrace();
            return "Tavsiye alınırken hata oluştu.";
        }
    }

    public String getMLBasedAdvice(int age, String location, String floorType) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "python", "src/main/python/predict_advice.py",
                    String.valueOf(age), location, floorType
            );
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            process.waitFor();
            return output.toString(); // JSON string {"advice": "..."}
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"ML tabanlı tavsiye alınamadı\"}";
        }
    }


}
