package com.fasocarbu.fasocarbu.modules.entreprise.repository;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByNom(String nom);
}
