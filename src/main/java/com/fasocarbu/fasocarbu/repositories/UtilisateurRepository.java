package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fasocarbu.fasocarbu.enums.Role;

import java.util.Optional;
import java.util.List;

import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);

    List<Utilisateur> findByRole(Role role);

    List<Utilisateur> findByEntreprise_IdAndRole(Long entrepriseId, Role role);

    List<Utilisateur> findByEntreprise_Id(Long entrepriseId);

}
