package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.LoginRequest;
import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.dtos.ChangePasswordRequest;
import com.fasocarbu.fasocarbu.dtos.JwtResponse;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.security.jwt.JwtUtils;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getMotDePasse()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        // 🔹 Sauvegarder le FCM token dans l'utilisateur
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            Utilisateur newUser = utilisateurService.registerUser(registerRequest);

            // 🔹 Préparer un body JSON clair
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

    @GetMapping("/ping")
    public String ping() {
        return "🔐 Auth API FasoCarbu est réveillée et OK !";
    }

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
