package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Station;import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByNomAndVilleAndAdresse(String nom, String adresse, String ville);
}

