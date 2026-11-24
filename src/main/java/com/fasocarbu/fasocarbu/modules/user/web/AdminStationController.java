package com.fasocarbu.fasocarbu.modules.user.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.service.CarburantService;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.ticket.service.TicketService;
import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasocarbu.fasocarbu.modules.user.domain.AgentStation;
import com.fasocarbu.fasocarbu.modules.user.dto.AdminStationDTO;
import com.fasocarbu.fasocarbu.modules.user.dto.CreateUserRequest;
import com.fasocarbu.fasocarbu.modules.user.dto.UtilisateurDTO;
import com.fasocarbu.fasocarbu.modules.user.service.AdminStationService;
import com.fasocarbu.fasocarbu.modules.user.service.AgentStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Admin Station", description = "Endpoints de gestion pour les administrateurs de station")
@RestController
@RequestMapping("/api/admin-stations")
@CrossOrigin(origins = "*")
public class AdminStationController {

    private final AdminStationService adminStationService;
    private final AgentStationService agentStationService;
    private final CarburantService carburantService;
    private final TicketService ticketService;

    public AdminStationController(AdminStationService adminStationService,
                                  AgentStationService agentStationService,
                                  CarburantService carburantService,
                                  TicketService ticketService) {
        this.adminStationService = adminStationService;
        this.agentStationService = agentStationService;
        this.carburantService = carburantService;
        this.ticketService = ticketService;
    }

    @Operation(summary = "Créer un AdminStation pour une station donnée")
    @PostMapping("/{stationId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public AdminStation createAdminStation(
            @RequestBody AdminStation adminStation,
            @PathVariable Long stationId) {
        return adminStationService.create(adminStation, stationId);
    }

    @Operation(summary = "Lister tous les AdminStations")
    @GetMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public List<AdminStation> getAllAdminStations() {
        return adminStationService.getAll();
    }

    @Operation(summary = "Obtenir un AdminStation par ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','ADMIN_STATION')")
    public AdminStation getAdminStationById(@PathVariable UUID id) {
        return adminStationService.getById(id);
    }

    @Operation(summary = "Mettre à jour un AdminStation")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public AdminStation updateAdminStation(
            @PathVariable UUID id,
            @RequestBody AdminStation adminStation) {
        return adminStationService.update(id, adminStation);
    }

    @Operation(summary = "Supprimer un AdminStation")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void deleteAdminStation(@PathVariable UUID id) {
        adminStationService.delete(id);
    }

    @Operation(summary = "Créer un Agent pour la station gérée par l'admin")
    @PostMapping("/{adminStationId}/agents")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<UtilisateurDTO> createAgent(
            @PathVariable UUID adminStationId,
            @RequestBody CreateUserRequest request) {
        AgentStation agent = agentStationService.createAgent(adminStationId, request);
        return ResponseEntity.ok(new UtilisateurDTO(agent));
    }

    @Operation(summary = "Lister les agents d'une AdminStation")
    @GetMapping("/{adminStationId}/agents")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<AgentStation> getAgentsByAdminStation(@PathVariable UUID adminStationId) {
        return agentStationService.getAgentsByAdminStation(adminStationId);
    }

    @Operation(summary = "Supprimer un agent de station")
    @DeleteMapping("/agents/{agentId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public void deleteAgent(@PathVariable UUID agentId) {
        agentStationService.deleteAgent(agentId);
    }

    @Operation(summary = "Ajouter un type de carburant pour une station")
    @PostMapping("/carburants")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public Carburant ajouterCarburant(
            @RequestBody Carburant carburant,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID adminStationId = userDetails.getId();
        return carburantService.ajouterCarburantPourStation(adminStationId, carburant);
    }

    @Operation(summary = "Lister les carburants gérés par l'admin station")
    @GetMapping("/{adminStationId}/carburants")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<Carburant> getCarburants(@PathVariable UUID adminStationId) {
        return carburantService.getCarburantsByAdminStation(adminStationId);
    }

    @Operation(summary = "Mettre à jour un carburant")
    @PutMapping("/{adminStationId}/carburants/{carburantId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<Carburant> updateCarburant(
            @PathVariable UUID adminStationId,
            @PathVariable Long carburantId,
            @RequestBody Carburant carburant) {
        Carburant updated = carburantService.updateCarburant(adminStationId, carburantId, carburant);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Supprimer un carburant")
    @DeleteMapping("/carburants/{carburantId}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public void deleteCarburant(@PathVariable Long carburantId) {
        carburantService.supprimerCarburant(carburantId);
    }

    @Operation(summary = "Consulter les tickets validés par l'admin de station")
    @GetMapping("/{adminStationId}/tickets")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public List<TicketDTO> getTicketsByAdminStation(
            @PathVariable UUID adminStationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();
        return ticketService.getTicketsDTOByUtilisateurOuEntreprise(adminStationId, entrepriseId);
    }
}
