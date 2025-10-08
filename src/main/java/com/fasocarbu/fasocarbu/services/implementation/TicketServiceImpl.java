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

import java.math.BigDecimal;
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

    @Override
    public TicketDTO enregistrerTicket(Ticket ticket) {
        try {
            ticket.setCodeQr(qrCodeGenerator.generateQRCodeForTicket(ticket));
            ticket.setDateEmission(LocalDateTime.now());
            ticket.setStatut(StatutTicket.EN_ATTENTE);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du code QR", e);
        }
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketDTO::new)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));
    }

    @Override
    public void supprimerTicket(Long id) {
        if (!ticketRepository.existsById(id))
            throw new RuntimeException("Ticket introuvable");
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketDTO validerTicket(Long ticketId, UUID validateurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        if (ticket.getStatut() == StatutTicket.VALIDER)
            throw new RuntimeException("Ticket déjà validé !");

        Utilisateur validateur = utilisateurRepository.findById(validateurId)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        ticket.setValidateur(validateur);
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO attribuerTicket(Long ticketId, UUID chauffeurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        Utilisateur chauffeur = utilisateurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        ticket.setUtilisateur(chauffeur);
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public List<TicketDTO> getAllTicketsDTO() {
        return ticketRepository.findAll().stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    // ------------------- NOUVEAU FILTRAGE -------------------

    @Override
    public List<TicketDTO> getTicketsDTOByEntreprise(Long entrepriseId) {
        return ticketRepository.findByEntreprise_Id(entrepriseId).stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsDTOByUtilisateurOuEntreprise(UUID userId, Long entrepriseId) {
        return ticketRepository.findByUtilisateur_IdOrEntreprise_Id(userId, entrepriseId).stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsDTOByUtilisateurEtEntreprise(UUID userId, Long entrepriseId) {
        return ticketRepository.findByUtilisateur_IdAndEntreprise_Id(userId, entrepriseId).stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public TicketDTO validerTicketDansEntreprise(Long ticketId, UUID agentStationId, Long entrepriseId) {
        Ticket ticket = ticketRepository.findByIdAndEntreprise_Id(ticketId, entrepriseId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable ou hors entreprise"));

        if (ticket.getStatut() == StatutTicket.VALIDER)
            throw new RuntimeException("Ticket déjà validé !");

        Utilisateur validateur = utilisateurRepository.findById(agentStationId)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        ticket.setValidateur(validateur);
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO attribuerTicketDansEntreprise(Long ticketId, UUID chauffeurId, Long entrepriseId) {
        Ticket ticket = ticketRepository.findByIdAndEntreprise_Id(ticketId, entrepriseId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable ou hors entreprise"));

        Utilisateur chauffeur = utilisateurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

        ticket.setUtilisateur(chauffeur);
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO validerTicketParCodeQrEtMontantDansEntreprise(String codeQr, String montant, UUID agentStationId,
            Long entrepriseId) {
        Ticket ticket = ticketRepository.findByCodeQrAndEntreprise_Id(codeQr, entrepriseId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable ou hors entreprise"));

        if (ticket.getStatut() == StatutTicket.VALIDER)
            throw new RuntimeException("Ticket déjà validé !");

        ticket.setMontant(new BigDecimal(montant));
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        Utilisateur validateur = utilisateurRepository.findById(agentStationId)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        ticket.setValidateur(validateur);

        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public List<TicketDTO> getTicketsValidesByUtilisateurEtEntreprise(UUID userId, Long entrepriseId) {
        return getTicketsDTOByUtilisateurEtEntreprise(userId, entrepriseId).stream()
                .filter(ticket -> ticket.getStatut() == StatutTicket.VALIDER)
                .collect(Collectors.toList());
    }
}
