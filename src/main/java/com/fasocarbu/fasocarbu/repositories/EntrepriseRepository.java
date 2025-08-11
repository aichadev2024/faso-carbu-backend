package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByNom(String nom);
}
