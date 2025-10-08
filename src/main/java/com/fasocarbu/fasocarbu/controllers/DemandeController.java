package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
import com.fasocarbu.fasocarbu.dtos.DemandeResponse;
import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import com.fasocarbu.fasocarbu.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest dto, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Utilisateur non trouvé");
        }

        Utilisateur user = utilisateurOpt.get();

        try {
            Demande demande = demandeService.creerDemandeAvecTicket(
                    user.getId(),
                    dto.getCarburantId(),
                    dto.getStationId(),
                    dto.getVehiculeId(),
                    dto.getQuantite());

            return ResponseEntity.status(HttpStatus.CREATED).body(new DemandeResponse(demande));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Erreur lors de la création de la demande : " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<?> getDemandes(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (utilisateur.getRole() == Role.GESTIONNAIRE) {
            // ✅ gestionnaire : toutes les demandes de son entreprise
            Long entrepriseId = utilisateur.getEntreprise().getId();

            return ResponseEntity.ok(
                    demandeService.getDemandesParEntreprise(entrepriseId)
                            .stream()
                            .map(DemandeResponse::new)
                            .toList());

        } else if (utilisateur.getRole() == Role.DEMANDEUR) {
            // ✅ demandeur : seulement ses propres demandes
            return ResponseEntity.ok(
                    demandeService.getDemandesParDemandeur(utilisateur.getId())
                            .stream()
                            .map(DemandeResponse::new)
                            .toList());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Accès refusé");
    }

}
