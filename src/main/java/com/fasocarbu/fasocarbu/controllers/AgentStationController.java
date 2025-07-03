package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.services.interfaces.AgentStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agentstation")
@CrossOrigin(origins = "*")
public class AgentStationController {

    @Autowired
    private AgentStationService agentStationService;

    @PostMapping("/register")
    public ResponseEntity<AgentStation> createAgent(@RequestBody AgentStation agentStation) {
        AgentStation savedAgent = agentStationService.enregistrerAgent(agentStation);
        return ResponseEntity.ok(savedAgent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentStation> getAgentById(@PathVariable Integer id) {
        AgentStation agent = agentStationService.getAgentById(id);
        if(agent == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(agent);
    }

    @GetMapping("/")
    public List<AgentStation> getAllAgents() {
        return agentStationService.getAllAgents();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Integer id) {
        agentStationService.supprimerAgent(id);
        return ResponseEntity.noContent().build();
    }
}
