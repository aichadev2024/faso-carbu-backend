package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.enums.StatutTicket;
import com.fasocarbu.fasocarbu.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodeQr(String codeQr);

    List<Ticket> findByStatut(StatutTicket statut);

    List<Ticket> findByUtilisateur_Id(UUID utilisateurId);

    List<Ticket> findByStationId(Long stationId);

    // ✅ Tickets par chauffeur via Attribution
    List<Ticket> findByAttribution_Chauffeur_Id(UUID chauffeurId);

    // ✅ Tickets par chauffeur entre 2 dates
    List<Ticket> findByAttribution_Chauffeur_IdAndDateEmissionBetween(
            UUID chauffeurId,
            LocalDateTime dateDebut,
            LocalDateTime dateFin);

    List<Ticket> findByEntreprise_Id(Long entrepriseId);

    List<Ticket> findByUtilisateur_IdOrEntreprise_Id(UUID utilisateurId, Long entrepriseId);

    List<Ticket> findByUtilisateur_IdAndEntreprise_Id(UUID utilisateurId, Long entrepriseId);

    Optional<Ticket> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

    Optional<Ticket> findByCodeQrAndEntreprise_Id(String codeQr, Long entrepriseId);

}
