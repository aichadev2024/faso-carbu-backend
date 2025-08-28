package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.enums.StatutTicket;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.TicketRepository;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import com.fasocarbu.fasocarbu.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    // ✅ Création ticket
    @Override
    public TicketDTO enregistrerTicket(Ticket ticket) {
        try {
            String codeQr = qrCodeGenerator.generateQRCodeForTicket(ticket);
            ticket.setCodeQr(codeQr);
            ticket.setDateEmission(LocalDateTime.now());
            ticket.setStatut(StatutTicket.EN_ATTENTE);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du code QR", e);
        }
        return new TicketDTO(ticketRepository.save(ticket));
    }

    // ✅ Récupérer un ticket par ID
    @Override
    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));
        return new TicketDTO(ticket);
    }

    // ✅ Supprimer un ticket
    @Override
    public void supprimerTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    // ✅ Valider ticket (agent station)
    @Override
    public TicketDTO validerTicket(Long ticketId, UUID validateurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        Utilisateur validateur = utilisateurRepository.findById(validateurId)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        ticket.setValidateur(validateur);
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        return new TicketDTO(ticketRepository.save(ticket));
    }

    // ✅ Attribuer ticket à un chauffeur
    @Override
    public TicketDTO attribuerTicket(Long ticketId, UUID chauffeurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        Utilisateur chauffeur = utilisateurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        // 🚨 Ici tu peux sécuriser si besoin : vérifier que le chauffeur appartient
        // bien à l'entreprise du gestionnaire

        ticket.setUtilisateur(chauffeur); // Chauffeur reçoit le ticket

        return new TicketDTO(ticketRepository.save(ticket));
    }

    // ✅ Tous les tickets (gestionnaire)
    @Override
    public List<TicketDTO> getAllTicketsDTO() {
        return ticketRepository.findAll().stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ Tickets d’un utilisateur (demandeur ou chauffeur)
    @Override
    public List<TicketDTO> getTicketsByUtilisateurDTO(UUID utilisateurId) {
        return ticketRepository.findByUtilisateur_Id(utilisateurId).stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }
}
