package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import com.fasocarbu.fasocarbu.services.interfaces.ChauffeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chauffeurs")
public class ChauffeurController {

    @Autowired
    private ChauffeurService chauffeurService;

    @PostMapping("/ajouter")
    public Chauffeur ajouterChauffeur(@RequestBody Chauffeur chauffeur) {
        return chauffeurService.ajouterChauffeur(chauffeur);
    }

    @GetMapping("/{id}")
    public Chauffeur getChauffeur(@PathVariable Long id) {
        return chauffeurService.getChauffeur(id);
    }

    @GetMapping
    public List<Chauffeur> getTousLesChauffeurs() {
        return chauffeurService.getTousLesChauffeurs();
    }

    @PutMapping("/{id}")
    public Chauffeur updateChauffeur(@PathVariable Long id, @RequestBody Chauffeur chauffeur) {
        return chauffeurService.updateChauffeur(id, chauffeur);
    }

    @DeleteMapping("/{id}")
    public void supprimerChauffeur(@PathVariable Long id) {
        chauffeurService.supprimerChauffeur(id);
    }
}
