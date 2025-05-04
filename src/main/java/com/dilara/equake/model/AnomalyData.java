package com.dilara.equake.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anomalies")
public class AnomalyData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private Double vibrationX;
    private Double vibrationY;
    private Double vibrationZ;
    private Double score;
    private Boolean anomaly;

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getVibrationX() {
        return vibrationX;
    }

    public void setVibrationX(Double vibrationX) {
        this.vibrationX = vibrationX;
    }

    public Double getVibrationY() {
        return vibrationY;
    }

    public void setVibrationY(Double vibrationY) {
        this.vibrationY = vibrationY;
    }

    public Double getVibrationZ() {
        return vibrationZ;
    }

    public void setVibrationZ(Double vibrationZ) {
        this.vibrationZ = vibrationZ;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(Boolean anomaly) {
        this.anomaly = anomaly;
    }
}
