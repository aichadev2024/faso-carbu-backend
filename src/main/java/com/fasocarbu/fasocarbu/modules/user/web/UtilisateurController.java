package com.fasocarbu.fasocarbu.modules.user.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.notification.dto.UpdateFcmTokenRequest;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.dto.CreateUserRequest;
import com.fasocarbu.fasocarbu.modules.user.dto.UtilisateurDTO;
import com.fasocarbu.fasocarbu.modules.user.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Utilisateurs", description = "Gestion des profils utilisateurs, des tokens FCM et des photos de profil")
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Value("${upload.dir:${java.io.tmpdir}/uploads}")
    private String uploadDir;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Ajouter un utilisateur (Gestionnaire)")
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> ajouterUtilisateur(
            @RequestBody CreateUserRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String emailGestionnaire = userDetails.getUsername();
        Utilisateur savedUser = utilisateurService.creerUtilisateurParGestionnaire(request, emailGestionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @Operation(summary = "Obtenir un utilisateur par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable UUID id) {
        Utilisateur u = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(u);
    }

    @Operation(summary = "Lister les utilisateurs par entreprise ou gestionnaire")
    @GetMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE', 'DEMANDEUR')")
    public ResponseEntity<?> getUtilisateurs(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTIONNAIRE"))) {
            UUID gestionnaireId = userDetails.getId();
            List<Utilisateur> users = utilisateurService.getUtilisateursParGestionnaire(gestionnaireId);
            return ResponseEntity.ok(users);
        }

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DEMANDEUR"))) {
            Long entrepriseId = utilisateurService.getEntrepriseIdFromUser(userDetails.getId());
            List<UtilisateurDTO> chauffeurs = utilisateurService.getChauffeursByEntreprise(entrepriseId)
                    .stream()
                    .map(UtilisateurDTO::new)
                    .toList();
            return ResponseEntity.ok(chauffeurs);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Accès non autorisé pour cet utilisateur.");
    }

    @Operation(summary = "Supprimer un utilisateur")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Void> supprimerUtilisateur(@PathVariable UUID id) {
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtenir les informations du compte connecté")
    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(userDetails.getId());
        return ResponseEntity.ok(utilisateur);
    }

    @Operation(summary = "Mettre à jour le token FCM")
    @PostMapping("/update-token")
    public ResponseEntity<?> updateToken(
            @RequestBody UpdateFcmTokenRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID userId = userDetails.getId();
        utilisateurService.updateFcmToken(userId, request.getFcmToken());
        return ResponseEntity.ok("Token mis à jour avec succès !");
    }

    @Operation(summary = "Lister les chauffeurs d'une entreprise")
    @GetMapping("/chauffeurs/{entrepriseId}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE', 'DEMANDEUR')")
    public List<UtilisateurDTO> getChauffeursByEntreprise(@PathVariable Long entrepriseId) {
        return utilisateurService.getChauffeursByEntreprise(entrepriseId)
                .stream()
                .map(UtilisateurDTO::new)
                .toList();
    }

    @Operation(summary = "Uploader une photo de profil")
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhotoProfil(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {

        try {
            String photoUrl = utilisateurService.uploadPhotoProfil(id, file);
            return ResponseEntity.ok(Map.of("photoProfil", photoUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Récupérer la photo de profil locale")
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Path uploadPath = Paths.get(uploadDir);
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
