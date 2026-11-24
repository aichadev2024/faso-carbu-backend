package com.fasocarbu.fasocarbu.modules.consommation.repository;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsommationRepository extends JpaRepository<Consommation, UUID> {

    List<Consommation> findByAttribution_Ticket_Vehicule_Id(Long vehiculeId);

    List<Consommation> findByEntreprise_Id(Long entrepriseId);

    List<Consommation> findByEntreprise_IdAndAttribution_Ticket_Vehicule_IdAndDateConsommationBetween(
            Long entrepriseId,
            Long vehiculeId,
            LocalDateTime debut,
            LocalDateTime fin);

    List<Consommation> findByEntreprise_IdAndDateConsommationBetween(
            Long entrepriseId,
            LocalDateTime debut,
            LocalDateTime fin);
}
