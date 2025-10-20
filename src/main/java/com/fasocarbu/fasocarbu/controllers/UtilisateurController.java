package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import com.fasocarbu.fasocarbu.dtos.UpdateFcmTokenRequest;
import com.fasocarbu.fasocarbu.dtos.UtilisateurDTO;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // =================== Création utilisateur ===================
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> ajouterUtilisateur(
            @RequestBody CreateUserRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String emailGestionnaire = userDetails.getUsername();
        Utilisateur savedUser = utilisateurService.creerUtilisateurParGestionnaire(request, emailGestionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // =================== Récupération par ID ===================
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id) {
        Utilisateur u = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(u);
    }

    // =================== Liste des utilisateurs (Gestionnaire & Demandeur)
    // ===================
    @GetMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE', 'DEMANDEUR')")
    public ResponseEntity<?> getUtilisateurs(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        // Si c’est un gestionnaire → retourne ses utilisateurs
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTIONNAIRE"))) {
            UUID gestionnaireId = userDetails.getId();
            List<Utilisateur> users = utilisateurService.getUtilisateursParGestionnaire(gestionnaireId);
            return ResponseEntity.ok(users);
        }

        // Si c’est un demandeur → retourne les chauffeurs de son entreprise
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DEMANDEUR"))) {
            Long entrepriseId = utilisateurService.getEntrepriseIdFromUser(userDetails.getId());
            List<UtilisateurDTO> chauffeurs = utilisateurService.getChauffeursByEntreprise(entrepriseId)
                    .stream()
                    .map(UtilisateurDTO::new)
                    .toList();
            return ResponseEntity.ok(chauffeurs);
        }

        // 🟡 Si aucun rôle correspondant → renvoyer une réponse claire
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Accès non autorisé pour cet utilisateur.");
    }

    // =================== Suppression ===================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable UUID id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    // =================== Récupérer l’utilisateur connecté ===================
    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userDetails.getId());
        return ResponseEntity.ok(utilisateur);
    }

    // =================== FCM Token ===================
    @PostMapping("/update-token")
    public ResponseEntity<?> updateToken(
            @RequestBody UpdateFcmTokenRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getId();
        utilisateurService.updateFcmToken(userId, request.getFcmToken());
        return ResponseEntity.ok("Token mis à jour avec succès !");
    }

    // =================== Chauffeurs par entreprise (Gestionnaire & Demandeur)
    // ===================
    @GetMapping("/chauffeurs/{entrepriseId}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE', 'DEMANDEUR')")
    public List<UtilisateurDTO> getChauffeursByEntreprise(@PathVariable Long entrepriseId) {
        return utilisateurService.getChauffeursByEntreprise(entrepriseId)
                .stream()
                .map(UtilisateurDTO::new)
                .toList();
    }

    // =================== Upload photo profil ===================
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhotoProfil(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        try {
            String photoUrl = utilisateurService.uploadPhotoProfil(id, file);
            return ResponseEntity.ok(Map.of("photoProfil", photoUrl)); // ✅ renvoie le bon champ
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // =================== Récupérer la photo ===================
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Path uploadPath = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
        Path filePath = uploadPath.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(resource);
    }

}
