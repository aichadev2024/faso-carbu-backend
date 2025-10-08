package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Station;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByNomAndVilleAndAdresse(String nom, String adresse, String ville);

    List<Station> findByEntreprise_Id(Long entrepriseId);
}
