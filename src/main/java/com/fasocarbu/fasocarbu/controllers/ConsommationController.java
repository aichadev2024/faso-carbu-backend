package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Consommation;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.ConsommationService;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consommations")
public class ConsommationController {

    @Autowired
    private ConsommationService consommationService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // ------------------- Récupérer toutes les consommations pour le gestionnaire
    // connecté -------------------
    @GetMapping
    public ResponseEntity<?> getConsommations(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur user = userOpt.get();
        Long entrepriseId = user.getEntreprise().getId();

        List<Consommation> consommations = consommationService.getConsommationsParEntreprise(entrepriseId);
        return ResponseEntity.ok(consommations);
    }

    // ------------------- Filtrer par véhicule et période -------------------
    @GetMapping("/filtrer")
    public ResponseEntity<?> filtrerConsommations(
            Authentication authentication,
            @RequestParam(required = false) Long vehiculeId,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur user = userOpt.get();
        Long entrepriseId = user.getEntreprise().getId();

        LocalDate debut = (dateDebut != null) ? LocalDate.parse(dateDebut) : null;
        LocalDate fin = (dateFin != null) ? LocalDate.parse(dateFin) : null;

        List<Consommation> consommations = consommationService.filtrerConsommations(entrepriseId, vehiculeId, debut,
                fin);
        return ResponseEntity.ok(consommations);
    }

    // ------------------- Ajouter une consommation -------------------
    @PostMapping
    public ResponseEntity<?> createConsommation(@RequestBody Consommation consommation, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur user = userOpt.get();
        consommation.setEntreprise(user.getEntreprise());

        Consommation saved = consommationService.enregistrerConsommation(consommation);
        return ResponseEntity.ok(saved);
    }
}
