package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // ✅ Ajouter un ticket (gestionnaire)
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO ajouterTicket(@RequestBody Ticket ticket) {
        return ticketService.enregistrerTicket(ticket);
    }

    // ✅ Récupérer un ticket par ID
    @GetMapping("/{id}")
    public TicketDTO getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    // ✅ Voir TOUS les tickets (gestionnaire uniquement)
    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTousLesTickets() {
        return ticketService.getAllTicketsDTO();
    }

    // ✅ Voir MES tickets (demandeur ou chauffeur connecté)
    @GetMapping("/mes-tickets")
    @PreAuthorize("hasAnyRole('DEMANDEUR', 'CHAUFFEUR')")
    public List<TicketDTO> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        return ticketService.getTicketsByUtilisateurDTO(userId);
    }

    // ✅ Voir tickets d’un utilisateur spécifique (gestionnaire)
    @GetMapping("/utilisateur/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTicketsByUtilisateur(@PathVariable UUID id) {
        return ticketService.getTicketsByUtilisateurDTO(id);
    }

    // ✅ Validation d’un ticket (agent station)
    @PostMapping("/valider/{id}")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicket(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID validateurId = userDetails.getId();
        return ticketService.validerTicket(id, validateurId);
    }

    // ✅ Attribuer un ticket à un chauffeur (gestionnaire)
    @PutMapping("/attribuer/{ticketId}/{chauffeurId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO attribuerTicket(@PathVariable Long ticketId,
            @PathVariable UUID chauffeurId) {
        return ticketService.attribuerTicket(ticketId, chauffeurId);
    }

    // ✅ Validation d’un ticket par QR code avec montant (agent station connecté)
    @PostMapping("/valider")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicketParQr(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String codeQr = payload.get("codeQr") != null ? payload.get("codeQr").toString() : null;
        if (codeQr == null || codeQr.isEmpty()) {
            throw new RuntimeException("⚠️ codeQr requis");
        }

        String montant = payload.get("montant") != null ? payload.get("montant").toString() : null;
        if (montant == null || montant.isEmpty()) {
            throw new RuntimeException("⚠️ montant requis");
        }

        UUID agentStationId = userDetails.getId();
        return ticketService.validerTicketParCodeQrEtMontant(codeQr, montant, agentStationId);
    }

}
