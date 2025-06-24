package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur>findByEmail(String email);
}
