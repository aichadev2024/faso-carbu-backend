package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.enums.StatutTicket;
import com.fasocarbu.fasocarbu.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCodeQr(String codeQr);

    List<Ticket> findByStatut(StatutTicket statut);

    List<Ticket> findByUtilisateur_Id(UUID utilisateurId);

    List<Ticket> findByStationId(Long stationId);

    Optional<Ticket> findByQrCode(String qrCode);

}
