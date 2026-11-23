package com.fasocarbu.fasocarbu.modules.station.repository;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByNomAndVilleAndAdresse(String nom, String adresse, String ville);

    List<Station> findByEntreprise_Id(Long entrepriseId);
}
