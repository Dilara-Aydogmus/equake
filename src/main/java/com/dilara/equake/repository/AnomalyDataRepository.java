package com.dilara.equake.repository;

import com.dilara.equake.model.AnomalyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnomalyDataRepository extends JpaRepository<AnomalyData, Long> {
    // Ek sorgular eklenecekse buraya yazılır.
}
