package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*") // ou ton domaine exact
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @PostMapping
    public ResponseEntity<Demande> creerDemande(@RequestBody Demande demande) {
        if (demande == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("📩 Demande reçue : " + demande);
        Demande nouvelle = demandeService.creerDemande(demande);
        return ResponseEntity.status(201).body(nouvelle);
    }

    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }
}