package com.fasocarbu.fasocarbu.modules.auth.web;

import com.fasocarbu.fasocarbu.core.security.jwt.JwtUtils;
import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.auth.dto.ChangePasswordRequest;
import com.fasocarbu.fasocarbu.modules.auth.dto.JwtResponse;
import com.fasocarbu.fasocarbu.modules.auth.dto.LoginRequest;
import com.fasocarbu.fasocarbu.modules.auth.dto.RegisterRequest;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentification", description = "Endpoints de connexion, inscription et gestion des mots de passe")
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UtilisateurService utilisateurService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UtilisateurService utilisateurService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un jeton JWT")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getMotDePasse()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        Utilisateur user = userDetails.getUtilisateur();
        if (loginRequest.getFcmToken() != null && !loginRequest.getFcmToken().isEmpty()) {
            user.setFcmToken(loginRequest.getFcmToken());
            utilisateurService.save(user);
        }

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities().stream().findFirst().get().getAuthority(),
                userDetails.getNom(),
                userDetails.getPrenom(),
                userDetails.getUtilisateur().getTelephone()
        ));
    }

    @Operation(summary = "Inscription d'un gestionnaire", description = "Crée un gestionnaire et son entreprise. Les autres rôles sont créés par un utilisateur connecté.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            Utilisateur newUser = utilisateurService.registerUser(registerRequest);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Utilisateur créé avec succès !");
            responseBody.put("id", newUser.getId());
            responseBody.put("nom", newUser.getNom());
            responseBody.put("prenom", newUser.getPrenom());
            responseBody.put("email", newUser.getEmail());
            responseBody.put("role", newUser.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (RuntimeException e) {
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", "Erreur lors de la création : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
        }
    }

    @Operation(summary = "Test de disponibilité de l'API Auth")
    @GetMapping("/ping")
    public String ping() {
        return "🔐 Auth API FasoCarbu est réveillée et OK !";
    }

    @Operation(summary = "Changement de mot de passe")
    @PutMapping("/changer-mot-de-passe")
    public ResponseEntity<?> changerMotDePasse(@RequestBody ChangePasswordRequest request,
                                               @RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.replace("Bearer ", "");
            String email = jwtUtils.getEmailFromJwtToken(token);

            utilisateurService.changerMotDePasse(email, request.getAncienMotDePasse(), request.getNouveauMotDePasse());

            return ResponseEntity.ok().body("Mot de passe changé avec succès ✅");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @Operation(summary = "Demande de réinitialisation de mot de passe")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            utilisateurService.demanderResetMotDePasse(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Un code de réinitialisation a été envoyé à " + email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Operation(summary = "Valider la réinitialisation de mot de passe")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        String nouveauMotDePasse = request.get("nouveauMotDePasse");

        try {
            utilisateurService.resetMotDePasse(email, code, nouveauMotDePasse);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Mot de passe réinitialisé avec succès ✅");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
