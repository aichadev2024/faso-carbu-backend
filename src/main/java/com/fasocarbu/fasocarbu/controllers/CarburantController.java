package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.CarburantDTO;
import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carburants")
@CrossOrigin(origins = "*")
public class CarburantController {

    @Autowired
    private CarburantService carburantService;

    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public CarburantDTO ajouterCarburant(@RequestBody Carburant carburant) {
        Carburant saved = carburantService.ajouterCarburant(carburant);
        return carburantService.getCarburantDTOById(saved.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN_STATION','GESTIONNAIRE', 'DEMANDEUR')")
    public List<CarburantDTO> getAll() {
        return carburantService.getAllCarburantsDTO();
    }

    @PutMapping("/{idCarburant}/updatePrix/{idAdminStation}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<CarburantDTO> updatePrix(
            @PathVariable Long idCarburant,
            @PathVariable UUID idAdminStation,
            @RequestParam Double nouveauPrix) {

        CarburantDTO updated = carburantService.updatePrixDTO(idCarburant, idAdminStation, nouveauPrix);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN_STATION')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carburantService.supprimerCarburant(id);
        return ResponseEntity.noContent().build();
    }
}
