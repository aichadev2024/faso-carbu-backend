package com.fasocarbu.fasocarbu.modules.station.repository;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarburantRepository extends JpaRepository<Carburant, Long> {
    List<Carburant> findByStationId(Long stationId);

    List<Carburant> findByAdminStationEntrepriseId(Long entrepriseId);

    List<Carburant> findByAdminStationId(UUID adminStationId);
}
