package com.dilara.equake.service;

import com.dilara.equake.model.AnomalyData;
import com.dilara.equake.repository.AnomalyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnomalyService {

    private final AnomalyDataRepository repository;

    @Autowired
    public AnomalyService(AnomalyDataRepository repository) {
        this.repository = repository;
    }

    public AnomalyData save(AnomalyData anomaly) {
        return repository.save(anomaly);
    }

    public List<AnomalyData> findAll() {
        return repository.findAll();
    }
}
