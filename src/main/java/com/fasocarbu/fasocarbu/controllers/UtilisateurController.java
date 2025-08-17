package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.UpdateFcmTokenRequest;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // =================== Création utilisateur ===================
    // Seul le gestionnaire peut créer des utilisateurs
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> ajouterUtilisateur(
            @RequestBody Utilisateur utilisateur,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Récupérer le gestionnaire connecté
        Utilisateur gestionnaire = utilisateurService.getUtilisateurByEmail(userDetails.getUsername());

        // Associer l'entreprise du gestionnaire à l'utilisateur créé
        utilisateur.setEntreprise(gestionnaire.getEntreprise());

        // Enregistrer l'utilisateur
        Utilisateur savedUser = utilisateurService.enregistrerUtilisateur(utilisateur);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // =================== Récupération ===================
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id) {
        Utilisateur u = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(u);
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> users = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.ok(users);
    }

    // =================== Suppression ===================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')") // facultatif mais recommandé
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable UUID id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    // =================== FCM Token ===================
    @PostMapping("/update-token")
    public ResponseEntity<?> updateToken(@RequestBody UpdateFcmTokenRequest request) {
        utilisateurService.updateFcmToken(request.getUserId(), request.getFcmToken());
        return ResponseEntity.ok("Token mis à jour avec succès !");
    }
}
