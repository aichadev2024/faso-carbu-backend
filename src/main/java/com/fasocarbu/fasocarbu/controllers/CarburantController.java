package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.CarburantDTO;
import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carburants")
@CrossOrigin(origins = "*")
public class CarburantController {

    @Autowired
    private CarburantService carburantService;

    // =================== AJOUTER ===================
    @PostMapping("/ajouter")
    public CarburantDTO ajouterCarburant(@RequestBody Carburant carburant) {
        Carburant saved = carburantService.ajouterCarburant(carburant);
        // On réutilise la méthode existante DTO
        return carburantService.getCarburantDTOById(saved.getId());
    }

    // =================== LISTE ===================
    @GetMapping
    public List<CarburantDTO> getAll() {
        return carburantService.getAllCarburantsDTO();
    }

    // =================== GET BY ID ===================
    @GetMapping("/{id}")
    public CarburantDTO getById(@PathVariable Long id) {
        return carburantService.getCarburantDTOById(id);
    }

    // =================== SUPPRIMER ===================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carburantService.supprimerCarburant(id);
        return ResponseEntity.noContent().build();
    }

    // =================== METTRE À JOUR PRIX ===================
    @PutMapping("/{idCarburant}/updatePrix/{idAdminStation}")
    public ResponseEntity<CarburantDTO> updatePrix(
            @PathVariable Long idCarburant,
            @PathVariable UUID idAdminStation,
            @RequestParam Double nouveauPrix) {

        CarburantDTO updated = carburantService.updatePrixDTO(idCarburant, idAdminStation, nouveauPrix);
        return ResponseEntity.ok(updated);
    }
}
