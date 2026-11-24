package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.user.domain.Role;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    List<Utilisateur> findByRole(Role role);

    List<Utilisateur> findByEntreprise_IdAndRole(Long entrepriseId, Role role);

    List<Utilisateur> findByEntreprise_Id(Long entrepriseId);

    Optional<Utilisateur> findFirstByEntreprise_IdAndRole(Long entrepriseId, Role role);
}
