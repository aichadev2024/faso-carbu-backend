package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
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
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO ajouterTicket(@RequestBody Ticket ticket,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // 🔹 Récupérer l'utilisateur complet depuis la DB
        Utilisateur gestionnaire = utilisateurRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        ticket.setUtilisateur(gestionnaire);

        return ticketService.enregistrerTicket(ticket);
    }

    // ✅ Récupérer un ticket par ID
    @GetMapping("/{id}")
    public TicketDTO getTicket(@PathVariable Long id) {
        System.out.println("📥 Requête récupération ticket ID=" + id);
        return ticketService.getTicketById(id);
    }

    // ✅ Tous les tickets (gestionnaire) — filtrés par entreprise
    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTousLesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("📥 Récupération tickets entreprise ID=" + entrepriseId);
        return ticketService.getTicketsDTOByEntreprise(entrepriseId);
    }

    // ✅ Tickets pour l'utilisateur courant (chauffeur ou demandeur) — seulement ses
    // tickets ou ceux de son entreprise
    @GetMapping("/mes-tickets")
    @PreAuthorize("hasAnyRole('DEMANDEUR','CHAUFFEUR')")
    public List<TicketDTO> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("📥 Requête tickets utilisateur ID=" + userId + ", entreprise ID=" + entrepriseId);
        return ticketService.getTicketsDTOByUtilisateurOuEntreprise(userId, entrepriseId);
    }

    // ✅ Tickets par utilisateur (gestionnaire) — filtrés par entreprise
    @GetMapping("/utilisateur/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTicketsByUtilisateur(@PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("📥 Requête tickets utilisateur ID=" + id + " pour entreprise ID=" + entrepriseId);
        return ticketService.getTicketsDTOByUtilisateurEtEntreprise(id, entrepriseId);
    }

    // ✅ Valider un ticket par ID (agent de station) — seulement si ticket de son
    // entreprise
    @PostMapping("/valider/{id}")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicket(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID agentStationId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("✅ Validation ticket ID=" + id + " par agentStation=" + agentStationId +
                ", entreprise ID=" + entrepriseId);
        return ticketService.validerTicketDansEntreprise(id, agentStationId, entrepriseId);
    }

    // ✅ Attribuer un ticket à un chauffeur (gestionnaire) — seulement pour
    // chauffeur de son entreprise
    @PutMapping("/attribuer/{ticketId}/{chauffeurId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO attribuerTicket(@PathVariable Long ticketId,
            @PathVariable UUID chauffeurId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("🚗 Attribution ticket ID=" + ticketId + " à chauffeur=" + chauffeurId +
                ", entreprise ID=" + entrepriseId);
        return ticketService.attribuerTicketDansEntreprise(ticketId, chauffeurId, entrepriseId);
    }

    // ✅ Valider un ticket via QR + montant (agent de station) — seulement si ticket
    // de son entreprise
    @PostMapping("/valider")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicketParQr(@RequestBody Map<String, Object> payload,
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
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("✅ Validation du ticket par QR: codeQr=" + codeQr +
                ", montant=" + montant +
                ", agentStationId=" + agentStationId +
                ", entrepriseId=" + entrepriseId);

        return ticketService.validerTicketParCodeQrEtMontantDansEntreprise(codeQr, montant, agentStationId,
                entrepriseId);
    }

    // ✅ Tickets validés pour l'utilisateur courant (chauffeur ou agent de station)
    // — filtrés par entreprise et agents créés par admin
    @GetMapping("/mes-tickets-valides")
    @PreAuthorize("hasAnyRole('AGENT_STATION','CHAUFFEUR')")
    public List<TicketDTO> getMesTicketsValides(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        System.out.println("📥 Tickets validés pour utilisateur ID=" + userId + ", entreprise ID=" + entrepriseId);
        return ticketService.getTicketsValidesByUtilisateurEtEntreprise(userId, entrepriseId);
    }
}
