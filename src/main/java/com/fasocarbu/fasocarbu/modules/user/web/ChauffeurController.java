package com.fasocarbu.fasocarbu.modules.user.web;

import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import com.fasocarbu.fasocarbu.modules.user.service.ChauffeurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Chauffeur", description = "Endpoints de gestion des chauffeurs")
@RestController
@RequestMapping("/api/chauffeurs")
public class ChauffeurController {

    private final ChauffeurService chauffeurService;

    public ChauffeurController(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    @Operation(summary = "Ajouter un chauffeur")
    @PostMapping("/ajouter")
    public Chauffeur ajouterChauffeur(@RequestBody Chauffeur chauffeur) {
        return chauffeurService.ajouterChauffeur(chauffeur);
    }

    @Operation(summary = "Obtenir un chauffeur par ID")
    @GetMapping("/{id}")
    public Chauffeur getChauffeur(@PathVariable UUID id) {
        return chauffeurService.getChauffeur(id);
    }

    @Operation(summary = "Lister tous les chauffeurs")
    @GetMapping
    public List<Chauffeur> getTousLesChauffeurs() {
        return chauffeurService.getTousLesChauffeurs();
    }

    @Operation(summary = "Mettre à jour un chauffeur")
    @PutMapping("/{id}")
    public Chauffeur updateChauffeur(@PathVariable UUID id, @RequestBody Chauffeur chauffeur) {
        return chauffeurService.updateChauffeur(id, chauffeur);
    }

    @Operation(summary = "Supprimer un chauffeur")
    @DeleteMapping("/{id}")
    public void supprimerChauffeur(@PathVariable UUID id) {
        chauffeurService.supprimerChauffeur(id);
    }
}
