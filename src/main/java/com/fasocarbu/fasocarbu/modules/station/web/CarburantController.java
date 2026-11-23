package com.fasocarbu.fasocarbu.modules.station.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.dto.CarburantDTO;
import com.fasocarbu.fasocarbu.modules.station.service.CarburantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Carburants", description = "Endpoints de gestion des carburants et de leurs prix")
@RestController
@RequestMapping("/api/carburants")
@CrossOrigin(origins = "*")
public class CarburantController {

    private final CarburantService carburantService;

    public CarburantController(CarburantService carburantService) {
        this.carburantService = carburantService;
    }

    @Operation(summary = "Ajouter un carburant (Admin Station)")
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public CarburantDTO ajouterCarburant(
            @RequestBody Carburant carburant,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID adminStationId = userDetails.getId();
        Carburant saved = carburantService.ajouterCarburantPourStation(adminStationId, carburant);
        return carburantService.getCarburantDTOById(saved.getId());
    }

    @Operation(summary = "Lister les carburants de l'entreprise")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_STATION','GESTIONNAIRE', 'DEMANDEUR')")
    public List<CarburantDTO> getAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();
        return carburantService.getAllCarburantsDTOByEntreprise(entrepriseId);
    }

    @Operation(summary = "Mettre à jour le prix du carburant")
    @PutMapping("/{idCarburant}/updatePrix")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<CarburantDTO> updatePrix(
            @PathVariable Long idCarburant,
            @RequestParam Double nouveauPrix,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID adminStationId = userDetails.getId();
        CarburantDTO updated = carburantService.updatePrixDTO(idCarburant, adminStationId, nouveauPrix);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Mettre à jour un carburant")
    @PutMapping("/{idCarburant}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<CarburantDTO> updateCarburant(
            @PathVariable Long idCarburant,
            @RequestBody Carburant carburant,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID adminStationId = userDetails.getId();
        Carburant updated = carburantService.updateCarburant(adminStationId, idCarburant, carburant);
        return ResponseEntity.ok(carburantService.getCarburantDTOById(updated.getId()));
    }

    @Operation(summary = "Supprimer un carburant")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carburantService.supprimerCarburant(id);
        return ResponseEntity.noContent().build();
    }
}
