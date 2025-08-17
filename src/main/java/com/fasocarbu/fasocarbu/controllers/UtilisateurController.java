package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.UpdateFcmTokenRequest;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // Création d'un utilisateur - uniquement pour GESTIONNAIRE
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void ajouterUtilisateur(@RequestBody Utilisateur utilisateur) {
        utilisateurService.enregistrerUtilisateur(utilisateur);
    }

    @GetMapping("/{id}")
    public Utilisateur getUtilisateurById(@PathVariable UUID id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')") // facultatif mais recommandé
    public void supprimerUtilisateur(@PathVariable UUID id) {
        utilisateurService.supprimerUtilisateur(id);
    }

    @PostMapping("/update-token")
    public ResponseEntity<?> updateToken(@RequestBody UpdateFcmTokenRequest request) {
        utilisateurService.updateFcmToken(request.getUserId(), request.getFcmToken());
        return ResponseEntity.ok("Token mis à jour avec succès !");
    }
}
