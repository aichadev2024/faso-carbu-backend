package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.services.interfaces.AgentStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/agent-stations")
@CrossOrigin(origins = "*")
public class AgentStationController {

    @Autowired
    private AgentStationService agentStationService;

    @PostMapping("/{adminStationId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public AgentStation createAgent(
            @PathVariable UUID adminStationId,
            @RequestBody AgentStation agent) {
        return agentStationService.createAgent(adminStationId, agent);
    }

    @GetMapping("/{adminStationId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<AgentStation> getAgentsByAdminStation(@PathVariable UUID adminStationId) {
        return agentStationService.getAgentsByAdminStation(adminStationId);
    }

    @DeleteMapping("/{agentId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public void deleteAgent(@PathVariable UUID agentId) {
        agentStationService.deleteAgent(agentId);
    }
}
