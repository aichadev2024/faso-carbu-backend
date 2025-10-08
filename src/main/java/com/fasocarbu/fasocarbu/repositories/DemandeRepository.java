package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DemandeRepository extends JpaRepository<Demande, Long> {

    // Par ID du demandeur
    List<Demande> findByDemandeur_Id(UUID demandeurId);

    // Par statut
    List<Demande> findByStatut(StatutDemande statut);

    // Par objet demandeur
    List<Demande> findByDemandeur(Utilisateur demandeur);

    // Par ID du gestionnaire
    List<Demande> findByGestionnaire_Id(UUID gestionnaireId);

    // Par ID de l'entreprise
    List<Demande> findByEntreprise_Id(Long entrepriseId);

    // Par ID et ID entreprise
    Optional<Demande> findByIdAndEntreprise_Id(Long id, Long entrepriseId);
}
