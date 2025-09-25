package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import com.fasocarbu.fasocarbu.dtos.UpdateFcmTokenRequest;
import com.fasocarbu.fasocarbu.dtos.UtilisateurDTO;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;

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

        // Récupérer l'email du gestionnaire connecté
        String emailGestionnaire = userDetails.getUsername();

        // Créer l'utilisateur en utilisant le service et le DTO
        Utilisateur savedUser = utilisateurService.creerUtilisateurParGestionnaire(request, emailGestionnaire);

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

        // Récupère directement l'ID de l'utilisateur connecté via JWT
        UUID userId = userDetails.getId();

        utilisateurService.updateFcmToken(userId, request.getFcmToken());

        return ResponseEntity.ok("Token mis à jour avec succès !");
    }

    @GetMapping("/chauffeurs/{entrepriseId}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
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
            return ResponseEntity.ok().body("{\"photoUrl\":\"" + photoUrl + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"" + e.getMessage() + "\"}");
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