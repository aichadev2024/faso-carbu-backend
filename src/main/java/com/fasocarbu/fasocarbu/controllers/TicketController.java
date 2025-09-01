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
        System.out.println("📥 Reçu ajout ticket: " + ticket);
        return ticketService.enregistrerTicket(ticket);
    }

    @GetMapping("/{id}")
    public TicketDTO getTicket(@PathVariable Long id) {
        System.out.println("📥 Requête récupération ticket ID=" + id);
        return ticketService.getTicketById(id);
    }

    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTousLesTickets() {
        System.out.println("📥 Requête récupération de tous les tickets");
        return ticketService.getAllTicketsDTO();
    }

    @GetMapping("/mes-tickets")
    @PreAuthorize("hasAnyRole('DEMANDEUR', 'CHAUFFEUR')")
    public List<TicketDTO> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        System.out.println("📥 Requête mes tickets pour utilisateur ID=" + userId);
        return ticketService.getTicketsByUtilisateurDTO(userId);
    }

    @GetMapping("/utilisateur/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTicketsByUtilisateur(@PathVariable UUID id) {
        System.out.println("📥 Requête tickets par utilisateur ID=" + id);
        return ticketService.getTicketsByUtilisateurDTO(id);
    }

    @PostMapping("/valider/{id}")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicket(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID validateurId = userDetails.getId();
        System.out.println("✅ Validation ticket ID=" + id + " par agentStation=" + validateurId);
        return ticketService.validerTicket(id, validateurId);
    }

    @PutMapping("/attribuer/{ticketId}/{chauffeurId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO attribuerTicket(@PathVariable Long ticketId,
            @PathVariable UUID chauffeurId) {
        System.out.println("🚗 Attribution ticket ID=" + ticketId + " à chauffeur=" + chauffeurId);
        return ticketService.attribuerTicket(ticketId, chauffeurId);
    }

    @PostMapping("/valider")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicketParQr(
            @RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        System.out.println("📥 Payload reçu pour validation QR: " + payload);

        String codeQr = payload.get("codeQr") != null ? payload.get("codeQr").toString() : null;
        if (codeQr == null || codeQr.isEmpty()) {
            System.out.println("⚠️ Erreur: codeQr manquant !");
            throw new RuntimeException("⚠️ codeQr requis");
        }

        String montant = payload.get("montant") != null ? payload.get("montant").toString() : null;
        if (montant == null || montant.isEmpty()) {
            System.out.println("⚠️ Erreur: montant manquant !");
            throw new RuntimeException("⚠️ montant requis");
        }

        UUID agentStationId = userDetails.getId();
        System.out.println("✅ Validation du ticket par QR: codeQr=" + codeQr +
                ", montant=" + montant +
                ", agentStationId=" + agentStationId);

        return ticketService.validerTicketParCodeQrEtMontant(codeQr, montant, agentStationId);
    }

}
