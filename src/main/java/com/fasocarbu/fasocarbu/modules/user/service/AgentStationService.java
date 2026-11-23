package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.user.domain.AgentStation;
import com.fasocarbu.fasocarbu.modules.user.dto.CreateUserRequest;

import java.util.List;
import java.util.UUID;

public interface AgentStationService {
    AgentStation createAgent(UUID adminStationId, CreateUserRequest request);

    List<AgentStation> getAgentsByAdminStation(UUID adminStationId);

    void deleteAgent(UUID agentId);

    Consommation scannerTicket(String codeQr, double quantiteUtilisee, String commentaire);
}
