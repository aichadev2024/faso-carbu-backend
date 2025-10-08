package com.fasocarbu.fasocarbu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fasocarbu.fasocarbu.models.Consommation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsommationRepository extends JpaRepository<Consommation, UUID> {

    List<Consommation> findByAttribution_Ticket_Vehicule_Id(long vehiculeId);

    List<Consommation> findByEntreprise_Id(Long entrepriseId);

    Optional<Consommation> findByIdAndEntreprise_Id(UUID id, Long entrepriseId);

    List<Consommation> findByAttribution_Ticket_Vehicule_Entreprise_Id(Long entrepriseId);

}
