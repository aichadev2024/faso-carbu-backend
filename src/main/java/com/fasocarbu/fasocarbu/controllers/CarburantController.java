package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.CarburantDTO;
import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carburants")
@CrossOrigin(origins = "*")
public class CarburantController {

    @Autowired
    private CarburantService carburantService;

    // ---------------- AJOUTER CARBURANT ----------------
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public CarburantDTO ajouterCarburant(
            @RequestBody Carburant carburant,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID adminStationId = userDetails.getId();
        Carburant saved = carburantService.ajouterCarburantPourStation(adminStationId, carburant);
        return carburantService.getCarburantDTOById(saved.getId());
    }

    // ---------------- LISTER CARBURANTS FILTRE ENTREPRISE ----------------
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_STATION','GESTIONNAIRE', 'DEMANDEUR')")
    public List<CarburantDTO> getAll(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();
        return carburantService.getAllCarburantsDTOByEntreprise(entrepriseId);
    }

    // ---------------- METTRE À JOUR LE PRIX ----------------
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

    // ---------------- SUPPRIMER CARBURANT ----------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carburantService.supprimerCarburant(id);
        return ResponseEntity.noContent().build();
    }
}
