package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/ajouter")
    public Ticket ajouterTicket(@RequestBody Ticket ticket) {
        return ticketService.enregistrerTicket(ticket);
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // ✅ Voir MES tickets (pour un demandeur)
    @GetMapping("/mes-tickets")
    @PreAuthorize("hasRole('DEMANDEUR')")
    public List<Ticket> getMesTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID userId = userDetails.getId();
        return ticketService.getTicketsByUtilisateur(userId);
    }

    // ✅ Voir TOUS les tickets (gestionnaire)
    @GetMapping("/tous")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<Ticket> getTousLesTickets() {
        return ticketService.getAllTickets();
    }

    // ✅ Voir tickets d’un utilisateur spécifique (gestionnaire)
    @GetMapping("/utilisateur/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<Ticket> getTicketsByUtilisateur(@PathVariable UUID id) {
        return ticketService.getTicketsByUtilisateur(id);
    }

}
