package com.fasocarbu.fasocarbu.modules.ticket.service;

import com.fasocarbu.fasocarbu.core.util.QRCodeGenerator;
import com.fasocarbu.fasocarbu.modules.ticket.domain.StatutTicket;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.ticket.repository.TicketRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final QRCodeGenerator qrCodeGenerator;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             UtilisateurRepository utilisateurRepository,
                             QRCodeGenerator qrCodeGenerator) {
        this.ticketRepository = ticketRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public TicketDTO enregistrerTicket(Ticket ticket) {
        try {
            UUID createurId = ticket.getUtilisateur() != null ? ticket.getUtilisateur().getId() : null;
            if (createurId == null)
                throw new RuntimeException("Utilisateur (créateur du ticket) manquant");

            Utilisateur createur = utilisateurRepository.findById(createurId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            if (createur.getEntreprise() == null)
                throw new RuntimeException("L'utilisateur n'est associé à aucune entreprise !");
            ticket.setEntreprise(createur.getEntreprise());

            ticket.setCodeQr(qrCodeGenerator.generateQRCodeForTicket(ticket));
            ticket.setDateEmission(LocalDateTime.now());
            ticket.setStatut(StatutTicket.EN_ATTENTE);

            return new TicketDTO(ticketRepository.save(ticket));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du ticket : " + e.getMessage(), e);
        }
    }

    @Override
    public TicketDTO getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(TicketDTO::new)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));
    }

    @Override
    public List<TicketDTO> getTicketsValidesByUtilisateurEtEntreprise(UUID userId, Long entrepriseId, String role) {
        List<Ticket> tickets;

        if ("ROLE_CHAUFFEUR".equals(role)) {
            tickets = ticketRepository.findByUtilisateur_IdAndEntreprise_Id(userId, entrepriseId);
            tickets = tickets.stream()
                    .filter(t -> t.getStatut() == StatutTicket.VALIDER)
                    .toList();
        } else if ("ROLE_AGENT_STATION".equals(role)) {
            tickets = ticketRepository.findByValidateur_IdAndEntreprise_IdAndStatut(
                    userId,
                    entrepriseId,
                    StatutTicket.VALIDER);
        } else {
            tickets = List.of();
        }

        return tickets.stream()
                .map(TicketDTO::new)
                .collect(Collectors.toList());
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

        Utilisateur agent = utilisateurRepository.findById(agentStationId)
                .orElseThrow(() -> new RuntimeException("Agent introuvable"));
        if (!agent.getEntreprise().getId().equals(ticket.getEntreprise().getId())) {
            throw new RuntimeException("Vous ne pouvez valider que les tickets de votre entreprise !");
        }

        if (ticket.getStatut() == StatutTicket.VALIDER)
            throw new RuntimeException("Ticket déjà validé !");

        ticket.setValidateur(agent);
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

        Utilisateur validateur = utilisateurRepository.findById(agentStationId)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        if (!validateur.getEntreprise().getId().equals(ticket.getEntreprise().getId())) {
            throw new RuntimeException("Vous ne pouvez pas valider un ticket d'une autre entreprise !");
        }

        if (ticket.getStatut() == StatutTicket.VALIDER)
            throw new RuntimeException("Ticket déjà validé !");

        ticket.setMontant(new BigDecimal(montant));
        ticket.setDateValidation(LocalDateTime.now());
        ticket.setStatut(StatutTicket.VALIDER);
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
