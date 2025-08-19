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
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    public String home() {
        return "🚀 API FasoCarbu est en ligne avec succès !";
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
}
