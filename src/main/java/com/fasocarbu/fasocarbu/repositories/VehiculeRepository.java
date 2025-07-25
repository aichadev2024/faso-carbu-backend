package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByImmatriculation(String immatriculation);
}
