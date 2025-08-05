package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Gestionnaire;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    @Autowired
    private GestionnaireService service;

    @PostMapping("/ajouter")
    public Gestionnaire ajouter(@RequestBody Gestionnaire gestionnaire) {
        return service.ajouterGestionnaire(gestionnaire);
    }

    @GetMapping("/{id}")
    public Gestionnaire obtenir(@PathVariable UUID id) {
        return service.obtenirGestionnaire(id);
    }

    @GetMapping
    public List<Gestionnaire> obtenirTous() {
        return service.obtenirTousLesGestionnaires();
    }

    @PutMapping("/{id}")
    public Gestionnaire modifier(@PathVariable UUID id, @RequestBody Gestionnaire gestionnaire) {
        return service.modifierGestionnaire(id, gestionnaire);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable UUID id) {
        service.supprimerGestionnaire(id);
    }
}
