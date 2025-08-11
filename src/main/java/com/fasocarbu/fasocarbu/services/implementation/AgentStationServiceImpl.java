package com.fasocarbu.fasocarbu.services.implementation;

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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


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

    @Override
    public AgentStation createAgent(UUID adminStationId, AgentStation agent) {
        AdminStation admin = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new IllegalArgumentException("AdminStation introuvable"));

        agent.setStation(admin.getStation());
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
    consommation.setAttribution(ticket.getAttribution()); // lien avec l'attribution
    consommation.setCarburant(ticket.getDemande().getCarburant()); // carburant lié à la demande
    consommation.setDateConsommation(LocalDateTime.now());
    consommation.setQuantiteUtilisee(quantiteUtilisee);
    consommation.setCommentaire(commentaire);

    return consommationRepository.save(consommation);
}

}
