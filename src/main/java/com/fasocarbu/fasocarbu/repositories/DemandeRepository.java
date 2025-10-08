package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.models.Utilisateur;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDemandeur_Id(UUID demandeurId);

    List<Demande> findByStatut(StatutDemande statut);

    List<Demande> findByDemandeur(Utilisateur demandeur);

    List<Demande> findByGestionnaire_Id(UUID gestionnaireId);

    List<Demande> findByEntreprise_Id(Long entrepriseId);

    Optional<Demande> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

}
