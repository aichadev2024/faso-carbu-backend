package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Consommation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConsommationRepository extends JpaRepository<Consommation, UUID> {

    // Récupérer toutes les consommations d'un véhicule
    List<Consommation> findByAttribution_Ticket_Vehicule_Id(Long vehiculeId);

    // Récupérer toutes les consommations par entreprise
    List<Consommation> findByEntreprise_Id(Long entrepriseId);

    // Filtrer par entreprise, véhicule et période
    List<Consommation> findByEntreprise_IdAndAttribution_Ticket_Vehicule_IdAndDateConsommationBetween(
            Long entrepriseId,
            Long vehiculeId,
            java.time.LocalDateTime debut,
            java.time.LocalDateTime fin);

    // Variante pour filtrer seulement par entreprise et période
    List<Consommation> findByEntreprise_IdAndDateConsommationBetween(
            Long entrepriseId,
            java.time.LocalDateTime debut,
            java.time.LocalDateTime fin);
}
