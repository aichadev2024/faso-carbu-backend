package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import com.fasocarbu.fasocarbu.enums.StatutTicket;
import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.AdminStationRepository;
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

    @Autowired
    private AdminStationRepository adminStationRepository;

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

        Ticket saved = ticketRepository.save(ticket);
        return new TicketDTO(saved);
    }

    @Override
    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Ticket introuvable"));
        return new TicketDTO(ticket);
    }

    @Override
    public void supprimerTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("❌ Ticket introuvable");
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketDTO validerTicket(Long ticketId, UUID validateurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("❌ Ticket introuvable"));

        if (ticket.getStatut() == StatutTicket.VALIDER) {
            throw new RuntimeException("⚠️ Ticket déjà validé !");
        }

        Utilisateur validateur = utilisateurRepository.findById(validateurId)
                .orElseThrow(() -> new RuntimeException("❌ Validateur introuvable"));

        ticket.setValidateur(validateur);
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDTO attribuerTicket(Long ticketId, UUID chauffeurId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("❌ Ticket introuvable"));

        Utilisateur chauffeur = utilisateurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("❌ Chauffeur introuvable"));

        ticket.setUtilisateur(chauffeur);
        return new TicketDTO(ticketRepository.save(ticket));
    }

    @Override
    public List<TicketDTO> getAllTicketsDTO() {
        return ticketRepository.findAll().stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> getTicketsByUtilisateurDTO(UUID utilisateurId) {
        return ticketRepository.findByUtilisateur_Id(utilisateurId).stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getTicketsByAdminStation(UUID adminStationId) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("❌ AdminStation introuvable"));

        return ticketRepository.findByStationId(adminStation.getStation().getId());
    }

    @Override
    public TicketDTO validerTicketParCodeQrEtMontant(String codeQr, String montant, UUID agentStationId) {
        Ticket ticket = ticketRepository.findByCodeQr(codeQr)
                .orElseThrow(() -> new RuntimeException("⚠️ Ticket introuvable"));

        if (ticket.getStatut() == StatutTicket.VALIDER) {
            throw new RuntimeException("⚠️ Ticket déjà validé");
        }

        ticket.setMontant(new BigDecimal(montant));
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);

        Utilisateur validateur = utilisateurRepository.findById(agentStationId)
                .orElseThrow(() -> new RuntimeException("⚠️ Validateur introuvable"));
        ticket.setValidateur(validateur);

        Ticket saved = ticketRepository.save(ticket);
        return new TicketDTO(saved);
    }

    public List<TicketDTO> getTicketsValidesByUtilisateur(UUID utilisateurId) {
        return getTicketsByUtilisateurDTO(utilisateurId)
                .stream()
                .filter(ticket -> ticket.getStatut() == StatutTicket.VALIDER)

                .toList();
    }

}
