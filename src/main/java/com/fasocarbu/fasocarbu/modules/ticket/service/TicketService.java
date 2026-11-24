package com.fasocarbu.fasocarbu.modules.ticket.service;

import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;

import java.util.List;
import java.util.UUID;

public interface TicketService {

    TicketDTO enregistrerTicket(Ticket ticket);

    TicketDTO getTicketById(Long id);

    void supprimerTicket(Long id);

    TicketDTO validerTicket(Long ticketId, UUID validateurId);

    TicketDTO attribuerTicket(Long ticketId, UUID chauffeurId);

    List<TicketDTO> getTicketsDTOByEntreprise(Long entrepriseId);

    List<TicketDTO> getTicketsDTOByUtilisateurOuEntreprise(UUID userId, Long entrepriseId);

    List<TicketDTO> getTicketsDTOByUtilisateurEtEntreprise(UUID userId, Long entrepriseId);

    TicketDTO validerTicketDansEntreprise(Long ticketId, UUID agentStationId, Long entrepriseId);

    TicketDTO attribuerTicketDansEntreprise(Long ticketId, UUID chauffeurId, Long entrepriseId);

    TicketDTO validerTicketParCodeQrEtMontantDansEntreprise(String codeQr, String montant, UUID agentStationId,
                                                           Long entrepriseId);

    List<TicketDTO> getAllTicketsDTO();

    List<TicketDTO> getTicketsValidesByUtilisateurEtEntreprise(UUID userId, Long entrepriseId);

    List<TicketDTO> getTicketsValidesByUtilisateurEtEntreprise(UUID userId, Long entrepriseId, String role);
}
