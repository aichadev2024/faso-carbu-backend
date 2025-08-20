package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.VehiculeDTO;
import com.fasocarbu.fasocarbu.models.Vehicule;
import com.fasocarbu.fasocarbu.services.interfaces.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    @PostMapping("/ajouter")
    public VehiculeDTO ajouterVehicule(@RequestBody Vehicule vehicule) {
        Vehicule savedVehicule = vehiculeService.enregistrerVehicule(vehicule);
        return new VehiculeDTO(savedVehicule);
    }

    @GetMapping("/{id}")
    public VehiculeDTO getVehicule(@PathVariable Long id) {
        Vehicule vehicule = vehiculeService.getVehiculeById(id);
        return new VehiculeDTO(vehicule);
    }

    @GetMapping
    public List<VehiculeDTO> getAllVehicules() {
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        return vehicules.stream()
                .map(VehiculeDTO::new)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerVehicule(id);
    }
}
