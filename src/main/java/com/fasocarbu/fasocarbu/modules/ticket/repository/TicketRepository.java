package com.fasocarbu.fasocarbu.modules.ticket.repository;

import com.fasocarbu.fasocarbu.modules.ticket.domain.StatutTicket;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByCodeQr(String codeQr);

    List<Ticket> findByStatut(StatutTicket statut);

    List<Ticket> findByUtilisateur_Id(UUID utilisateurId);

    List<Ticket> findByStationId(Long stationId);

    List<Ticket> findByAttribution_Chauffeur_Id(UUID chauffeurId);

    List<Ticket> findByAttribution_Chauffeur_IdAndDateEmissionBetween(
            UUID chauffeurId,
            LocalDateTime dateDebut,
            LocalDateTime dateFin);

    List<Ticket> findByEntreprise_Id(Long entrepriseId);

    List<Ticket> findByUtilisateur_IdOrEntreprise_Id(UUID utilisateurId, Long entrepriseId);

    List<Ticket> findByUtilisateur_IdAndEntreprise_Id(UUID utilisateurId, Long entrepriseId);

    Optional<Ticket> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

    Optional<Ticket> findByCodeQrAndEntreprise_Id(String codeQr, Long entrepriseId);

    List<Ticket> findByValidateur_IdAndEntreprise_IdAndStatut(
            UUID validateurId,
            Long entrepriseId,
            StatutTicket statut);

    List<Ticket> findByAttribution_Chauffeur_IdAndEntreprise_Id(UUID chauffeurId, Long entrepriseId);

    List<Ticket> findByAttribution_Chauffeur_IdAndDateEmissionBetweenAndEntreprise_Id(
            UUID chauffeurId, LocalDateTime debut, LocalDateTime fin, Long entrepriseId);
}
