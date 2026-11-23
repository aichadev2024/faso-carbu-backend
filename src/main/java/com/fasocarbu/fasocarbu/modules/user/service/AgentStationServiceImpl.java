package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.consommation.repository.ConsommationRepository;
import com.fasocarbu.fasocarbu.modules.ticket.domain.StatutTicket;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.repository.TicketRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasocarbu.fasocarbu.modules.user.domain.AgentStation;
import com.fasocarbu.fasocarbu.modules.user.domain.Role;
import com.fasocarbu.fasocarbu.modules.user.dto.CreateUserRequest;
import com.fasocarbu.fasocarbu.modules.user.repository.AdminStationRepository;
import com.fasocarbu.fasocarbu.modules.user.repository.AgentStationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AgentStationServiceImpl implements AgentStationService {

    private final AgentStationRepository agentStationRepository;
    private final TicketRepository ticketRepository;
    private final AdminStationRepository adminStationRepository;
    private final ConsommationRepository consommationRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentStationServiceImpl(AgentStationRepository agentStationRepository,
                                    TicketRepository ticketRepository,
                                    AdminStationRepository adminStationRepository,
                                    ConsommationRepository consommationRepository,
                                    PasswordEncoder passwordEncoder) {
        this.agentStationRepository = agentStationRepository;
        this.ticketRepository = ticketRepository;
        this.adminStationRepository = adminStationRepository;
        this.consommationRepository = consommationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AgentStation createAgent(UUID adminStationId, CreateUserRequest request) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("Admin Station introuvable"));

        AgentStation agent = new AgentStation();
        agent.setNom(request.getNom());
        agent.setPrenom(request.getPrenom());
        agent.setEmail(request.getEmail());
        agent.setTelephone(request.getTelephone());
        agent.setNomUtilisateur(request.getNomUtilisateur());
        agent.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        agent.setRole(Role.AGENT_STATION);

        agent.setStation(adminStation.getStation());
        agent.setEntreprise(adminStation.getEntreprise());

        return agentStationRepository.save(agent);
    }

    @Override
    public List<AgentStation> getAgentsByAdminStation(UUID adminStationId) {
        AdminStation admin = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new IllegalArgumentException("AdminStation introuvable"));

        return agentStationRepository.findByStation(admin.getStation());
    }

    @Override
    public void deleteAgent(UUID agentId) {
        agentStationRepository.deleteById(agentId);
    }

    @Override
    public Consommation scannerTicket(String codeQr, double quantiteUtilisee, String commentaire) {
        Ticket ticket = ticketRepository.findByCodeQr(codeQr)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        if (ticket.getStatut() == StatutTicket.CONSOMMEE) {
            throw new RuntimeException("Ce ticket a déjà été consommé");
        }

        ticket.setStatut(StatutTicket.CONSOMMEE);
        ticketRepository.save(ticket);

        Consommation consommation = new Consommation();
        consommation.setAttribution(ticket.getAttribution());
        consommation.setCarburant(ticket.getDemande() != null ? ticket.getDemande().getCarburant() : ticket.getCarburant());
        consommation.setDateConsommation(LocalDateTime.now());
        consommation.setQuantiteUtilisee(quantiteUtilisee);
        consommation.setCommentaire(commentaire);

        return consommationRepository.save(consommation);
    }
}
