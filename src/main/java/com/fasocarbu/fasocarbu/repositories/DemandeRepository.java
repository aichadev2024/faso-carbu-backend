package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.Demande;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDemandeur_Id(UUID demandeurId);
    List<Demande> findByStatut(StatutDemande statut);



}
