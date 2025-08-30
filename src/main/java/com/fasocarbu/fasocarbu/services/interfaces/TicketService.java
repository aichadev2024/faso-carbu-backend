package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.models.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketService {
    // Création d’un ticket
    TicketDTO enregistrerTicket(Ticket ticket);

    // Détails d’un ticket
    TicketDTO getTicketById(Long id);

    // Suppression
    void supprimerTicket(Long id);

    // Validation (agent station)
    TicketDTO validerTicket(Long ticketId, UUID validateurId);

    // Attribution (gestionnaire -> chauffeur)
    TicketDTO attribuerTicket(Long ticketId, UUID chauffeurId);

    // Tous les tickets (gestionnaire)
    List<TicketDTO> getAllTicketsDTO();

    // Tickets d’un utilisateur (demandeur ou chauffeur)
    List<TicketDTO> getTicketsByUtilisateurDTO(UUID utilisateurId);

    List<Ticket> getTicketsByAdminStation(UUID adminStationId);

    TicketDTO validerTicketParCodeQr(String qrCode, UUID agentStationId);

}
