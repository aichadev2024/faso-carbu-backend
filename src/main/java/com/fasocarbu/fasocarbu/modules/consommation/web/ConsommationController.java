package com.fasocarbu.fasocarbu.modules.consommation.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.consommation.service.ConsommationService;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Consommations", description = "Endpoints de suivi et de filtrage de la consommation de carburant")
@RestController
@RequestMapping("/api/consommations")
public class ConsommationController {

    private final ConsommationService consommationService;
    private final UtilisateurRepository utilisateurRepository;

    public ConsommationController(ConsommationService consommationService, UtilisateurRepository utilisateurRepository) {
        this.consommationService = consommationService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Operation(summary = "Lister toutes les consommations de l'entreprise")
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

    @Operation(summary = "Filtrer les consommations par véhicule et plage de dates")
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

        List<Consommation> consommations = consommationService.filtrerConsommations(entrepriseId, vehiculeId, debut, fin);
        return ResponseEntity.ok(consommations);
    }

    @Operation(summary = "Enregistrer une consommation")
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
