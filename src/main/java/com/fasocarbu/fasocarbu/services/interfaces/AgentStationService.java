package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.AgentStation;

import java.util.List;

public interface AgentStationService {
    AgentStation enregistrerAgent(AgentStation agentStation);
    AgentStation getAgentById(Integer id);
    List<AgentStation> getAllAgents();
    void supprimerAgent(Integer id);
}
