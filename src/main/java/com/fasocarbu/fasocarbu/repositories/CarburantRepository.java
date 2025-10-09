package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Carburant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CarburantRepository extends JpaRepository<Carburant, Long> {
    List<Carburant> findByStationId(Long stationId);

    List<Carburant> findByAdminStationEntrepriseId(Long entrepriseId);

    List<Carburant> findByAdminStationId(UUID adminStationId);
}
