package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UtilisateurRepository utilisateurRepository;

    // 🔹 Ajouter un ticket (gestionnaire)
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO ajouterTicket(@RequestBody Ticket ticket,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Récupérer l'utilisateur complet
        Utilisateur gestionnaire = utilisateurRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        ticket.setUtilisateur(gestionnaire);

        // Assigner automatiquement l'entreprise attachée
        if (gestionnaire.getEntreprise() == null) {
            throw new RuntimeException("L'utilisateur n'est associé à aucune entreprise !");
        }
        ticket.setEntreprise(gestionnaire.getEntreprise());

        return ticketService.enregistrerTicket(ticket);
    }

    // 🔹 Récupérer un ticket par ID
    @GetMapping("/{id}")
    public TicketDTO getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    // 🔹 Récupérer tous les tickets d'un gestionnaire, filtrés par entreprise
    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTousLesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.getTicketsDTOByEntreprise(entrepriseId);
    }

    // 🔹 Tickets pour l'utilisateur courant (chauffeur ou demandeur)
    @GetMapping("/mes-tickets")
    @PreAuthorize("hasAnyRole('DEMANDEUR','CHAUFFEUR')")
    public List<TicketDTO> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.getTicketsDTOByUtilisateurOuEntreprise(userId, entrepriseId);
    }

    // 🔹 Valider un ticket par agent de station
    @PostMapping("/valider/{id}")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicket(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID agentStationId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.validerTicketDansEntreprise(id, agentStationId, entrepriseId);
    }

    // 🔹 Attribuer un ticket à un chauffeur
    @PutMapping("/attribuer/{ticketId}/{chauffeurId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO attribuerTicket(@PathVariable Long ticketId,
            @PathVariable UUID chauffeurId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.attribuerTicketDansEntreprise(ticketId, chauffeurId, entrepriseId);
    }

    // 🔹 Valider un ticket via QR + montant
    @PostMapping("/valider")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicketParQr(@RequestBody Map<String, Object> payload,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String codeQr = payload.get("codeQr") != null ? payload.get("codeQr").toString() : null;
        String montant = payload.get("montant") != null ? payload.get("montant").toString() : null;

        if (codeQr == null || codeQr.isEmpty() || montant == null || montant.isEmpty()) {
            throw new RuntimeException("codeQr et montant sont requis");
        }

        UUID agentStationId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();

        return ticketService.validerTicketParCodeQrEtMontantDansEntreprise(codeQr, montant, agentStationId,
                entrepriseId);
    }

    @GetMapping("/mes-tickets-valides")
    @PreAuthorize("hasAnyRole('AGENT_STATION','CHAUFFEUR')")
    public List<TicketDTO> getMesTicketsValides(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ticketService.getTicketsValidesByUtilisateurEtEntreprise(userId, entrepriseId, role);
    }

}
