package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.models.Consommation;

import java.util.List;
import java.util.UUID;

public interface AgentStationService {
    AgentStation createAgent(UUID adminStationId, AgentStation agent);
    List<AgentStation> getAgentsByAdminStation(UUID adminStationId);
    void deleteAgent(UUID agentId);
    Consommation scannerTicket(String codeQr, double quantiteUtilisee, String commentaire);
}
