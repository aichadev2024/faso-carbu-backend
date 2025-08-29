package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.services.interfaces.AdminStationService;
import com.fasocarbu.fasocarbu.services.interfaces.AgentStationService;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import com.fasocarbu.fasocarbu.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin-stations")
@CrossOrigin(origins = "*")
public class AdminStationController {

    @Autowired
    private AdminStationService adminStationService;

    @Autowired
    private AgentStationService agentStationService;

    @Autowired
    private CarburantService carburantService;

    @Autowired
    private TicketService ticketService;

    // ------------------- CRUD ADMIN STATION -------------------

    @PostMapping("/{stationId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public AdminStation createAdminStation(
            @RequestBody AdminStation adminStation,
            @PathVariable Long stationId) {
        return adminStationService.create(adminStation, stationId);
    }

    @GetMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<AdminStation> getAllAdminStations() {
        return adminStationService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','ADMIN_STATION')")
    public AdminStation getAdminStationById(@PathVariable UUID id) {
        return adminStationService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public AdminStation updateAdminStation(
            @PathVariable UUID id,
            @RequestBody AdminStation adminStation) {
        return adminStationService.update(id, adminStation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void deleteAdminStation(@PathVariable UUID id) {
        adminStationService.delete(id);
    }

    // ------------------- GESTION DES AGENTS -------------------

    @PostMapping("/{adminStationId}/agents")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public AgentStation createAgent(
            @PathVariable UUID adminStationId,
            @RequestBody AgentStation agent) {
        return agentStationService.createAgent(adminStationId, agent);
    }

    @GetMapping("/{adminStationId}/agents")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<AgentStation> getAgentsByAdminStation(@PathVariable UUID adminStationId) {
        return agentStationService.getAgentsByAdminStation(adminStationId);
    }

    @DeleteMapping("/agents/{agentId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public void deleteAgent(@PathVariable UUID agentId) {
        agentStationService.deleteAgent(agentId);
    }

    // ------------------- GESTION DES CARBURANTS -------------------

    @PostMapping("/{adminStationId}/carburants")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public Carburant ajouterCarburant(@PathVariable UUID adminStationId, @RequestBody Carburant carburant) {
        return carburantService.ajouterCarburantPourStation(adminStationId, carburant);
    }

    @GetMapping("/{adminStationId}/carburants")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<Carburant> getCarburants(@PathVariable UUID adminStationId) {
        return carburantService.getCarburantsByAdminStation(adminStationId);
    }

    @PutMapping("/{adminStationId}/carburants/{carburantId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<Carburant> updateCarburant(
            @PathVariable UUID adminStationId,
            @PathVariable Long carburantId,
            @RequestBody Carburant carburant) {
        Carburant updated = carburantService.updateCarburant(adminStationId, carburantId, carburant);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/carburants/{carburantId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public void deleteCarburant(@PathVariable Long carburantId) {
        carburantService.supprimerCarburant(carburantId);
    }

    // ------------------- CONSULTATION DES TICKETS -------------------

    @GetMapping("/{adminStationId}/tickets")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<Ticket> getTicketsByAdminStation(@PathVariable UUID adminStationId) {
        return ticketService.getTicketsByAdminStation(adminStationId);
    }
}
