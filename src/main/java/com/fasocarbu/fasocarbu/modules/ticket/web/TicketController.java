package com.fasocarbu.fasocarbu.modules.ticket.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.ticket.service.TicketService;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Tickets", description = "Endpoints de gestion des tickets, validation QR et attributions")
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;
    private final UtilisateurRepository utilisateurRepository;

    public TicketController(TicketService ticketService, UtilisateurRepository utilisateurRepository) {
        this.ticketService = ticketService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Operation(summary = "Ajouter un ticket (Gestionnaire)")
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO ajouterTicket(@RequestBody Ticket ticket,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Utilisateur gestionnaire = utilisateurRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        ticket.setUtilisateur(gestionnaire);

        if (gestionnaire.getEntreprise() == null) {
            throw new RuntimeException("L'utilisateur n'est associé à aucune entreprise !");
        }
        ticket.setEntreprise(gestionnaire.getEntreprise());

        return ticketService.enregistrerTicket(ticket);
    }

    @Operation(summary = "Obtenir un ticket par ID")
    @GetMapping("/{id}")
    public TicketDTO getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @Operation(summary = "Récupérer tous les tickets d'une entreprise (Gestionnaire)")
    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<TicketDTO> getTousLesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.getTicketsDTOByEntreprise(entrepriseId);
    }

    @Operation(summary = "Récupérer les tickets du compte connecté (Demandeur/Chauffeur)")
    @GetMapping("/mes-tickets")
    @PreAuthorize("hasAnyRole('DEMANDEUR','CHAUFFEUR')")
    public List<TicketDTO> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.getTicketsDTOByUtilisateurOuEntreprise(userId, entrepriseId);
    }

    @Operation(summary = "Valider un ticket par un agent de station")
    @PostMapping("/valider/{id}")
    @PreAuthorize("hasRole('AGENT_STATION')")
    public TicketDTO validerTicket(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID agentStationId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.validerTicketDansEntreprise(id, agentStationId, entrepriseId);
    }

    @Operation(summary = "Attribuer un ticket à un chauffeur")
    @PutMapping("/attribuer/{ticketId}/{chauffeurId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public TicketDTO attribuerTicket(@PathVariable Long ticketId,
                                    @PathVariable UUID chauffeurId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.attribuerTicketDansEntreprise(ticketId, chauffeurId, entrepriseId);
    }

    @Operation(summary = "Valider un ticket par QR Code et montant")
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

        return ticketService.validerTicketParCodeQrEtMontantDansEntreprise(codeQr, montant, agentStationId, entrepriseId);
    }

    @Operation(summary = "Lister les tickets validés du compte connecté")
    @GetMapping("/mes-tickets-valides")
    @PreAuthorize("hasAnyRole('AGENT_STATION','CHAUFFEUR')")
    public List<TicketDTO> getMesTicketsValides(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        Long entrepriseId = userDetails.getEntrepriseId();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ticketService.getTicketsValidesByUtilisateurEtEntreprise(userId, entrepriseId, role);
    }
}
