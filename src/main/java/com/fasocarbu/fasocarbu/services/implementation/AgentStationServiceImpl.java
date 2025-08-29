package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import com.fasocarbu.fasocarbu.enums.Role;
import com.fasocarbu.fasocarbu.enums.StatutTicket;
import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.models.Consommation;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.repositories.AdminStationRepository;
import com.fasocarbu.fasocarbu.repositories.AgentStationRepository;
import com.fasocarbu.fasocarbu.repositories.ConsommationRepository;
import com.fasocarbu.fasocarbu.repositories.TicketRepository;
import com.fasocarbu.fasocarbu.services.interfaces.AgentStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AgentStationServiceImpl implements AgentStationService {

    @Autowired
    private AgentStationRepository agentStationRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AdminStationRepository adminStationRepository;

    @Autowired
    private ConsommationRepository consommationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ injection du passwordEncoder

    @Override
    public AgentStation createAgent(UUID adminStationId, CreateUserRequest request) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("Admin Station introuvable"));

        AgentStation agent = new AgentStation();
        agent.setNom(request.getNom());
        agent.setPrenom(request.getPrenom());
        agent.setEmail(request.getEmail());
        agent.setTelephone(request.getTelephone());
        agent.setMotDePasse(passwordEncoder.encode(request.getMotDePasse())); // ✅ sécurisé
        agent.setRole(Role.AGENT_STATION); // ✅ enum bien importé
        agent.setStation(adminStation.getStation()); // ✅ lien avec la station de l’admin

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
        // 1️⃣ Récupérer le ticket par code QR
        Ticket ticket = ticketRepository.findByCodeQr(codeQr)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        // 2️⃣ Vérifier que le ticket n’est pas déjà consommé
        if (ticket.getStatut() == StatutTicket.CONSOMMEE) {
            throw new RuntimeException("Ce ticket a déjà été consommé");
        }

        // 3️⃣ Marquer le ticket comme consommé
        ticket.setStatut(StatutTicket.CONSOMMEE);
        ticketRepository.save(ticket);

        // 4️⃣ Créer l’enregistrement de consommation
        Consommation consommation = new Consommation();
        consommation.setAttribution(ticket.getAttribution());
        consommation.setCarburant(ticket.getDemande().getCarburant());
        consommation.setDateConsommation(LocalDateTime.now());
        consommation.setQuantiteUtilisee(quantiteUtilisee);
        consommation.setCommentaire(commentaire);

        return consommationRepository.save(consommation);
    }
}
