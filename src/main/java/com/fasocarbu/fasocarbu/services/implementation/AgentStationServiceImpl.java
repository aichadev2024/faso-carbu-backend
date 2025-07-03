package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.repositories.AgentStationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.AgentStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentStationServiceImpl implements AgentStationService {

    @Autowired
    private AgentStationRepository agentStationRepository;

    @Override
    public AgentStation enregistrerAgent(AgentStation agentStation) {
        return agentStationRepository.save(agentStation);
    }

    @Override
    public AgentStation getAgentById(Integer id) {
        return agentStationRepository.findById(id).orElse(null);
    }

    @Override
    public List<AgentStation> getAllAgents() {
        return agentStationRepository.findAll();
    }

    @Override
    public void supprimerAgent(Integer id) {
        if(agentStationRepository.existsById(id)) {
            agentStationRepository.deleteById(id);
        }
    }
}
