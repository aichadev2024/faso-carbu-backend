package com.fasocarbu.fasocarbu.modules.demande.repository;

import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.demande.domain.StatutDemande;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {

    List<Demande> findByDemandeur_Id(UUID demandeurId);

    List<Demande> findByStatut(StatutDemande statut);

    List<Demande> findByDemandeur(Utilisateur demandeur);

    List<Demande> findByGestionnaire_Id(UUID gestionnaireId);

    List<Demande> findByEntreprise_Id(Long entrepriseId);

    Optional<Demande> findByIdAndEntreprise_Id(Long id, Long entrepriseId);
}
