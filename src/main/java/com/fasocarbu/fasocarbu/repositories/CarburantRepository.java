package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Carburant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarburantRepository extends JpaRepository<Carburant, Long> {
    List<Carburant> findByStationId(Long stationId);
}
