package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    // ✅ Seul un CHAUFFEUR peut faire une demande
    @PostMapping
    public ResponseEntity<?> creerDemande(@RequestBody Demande demande) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isChauffeur = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CHAUFFEUR"));

        if (!isChauffeur) {
            return ResponseEntity.status(403).body("❌ Seul un chauffeur peut faire une demande de ticket.");
        }

        Demande nouvelle = demandeService.creerDemande(demande);
        return ResponseEntity.status(201).body(nouvelle);
    }

    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }
}