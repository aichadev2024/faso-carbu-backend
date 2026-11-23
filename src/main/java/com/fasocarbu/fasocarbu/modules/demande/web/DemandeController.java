package com.fasocarbu.fasocarbu.modules.demande.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.demande.dto.DemandeRequest;
import com.fasocarbu.fasocarbu.modules.demande.dto.DemandeResponse;
import com.fasocarbu.fasocarbu.modules.demande.service.DemandeService;
import com.fasocarbu.fasocarbu.modules.user.domain.Role;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Demandes", description = "Endpoints de soumission et de suivi des demandes de carburant")
@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    private final DemandeService demandeService;
    private final UtilisateurRepository utilisateurRepository;

    public DemandeController(DemandeService demandeService, UtilisateurRepository utilisateurRepository) {
        this.demandeService = demandeService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Operation(summary = "Créer une demande de carburant (Demandeur/Gestionnaire)")
    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest dto, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Utilisateur non trouvé");
        }

        Utilisateur demandeur = utilisateurOpt.get();

        if (demandeur.getEntreprise() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Le demandeur n'est associé à aucune entreprise.");
        }

        try {
            Optional<Utilisateur> gestionnaireOpt = utilisateurRepository
                    .findFirstByEntreprise_IdAndRole(demandeur.getEntreprise().getId(), Role.GESTIONNAIRE);

            if (gestionnaireOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("❌ Aucun gestionnaire trouvé pour cette entreprise.");
            }

            Utilisateur gestionnaire = gestionnaireOpt.get();

            Demande demande = demandeService.creerDemande(
                    demandeur.getId(),
                    gestionnaire.getId(),
                    demandeur.getEntreprise().getId(),
                    dto.getCarburantId(),
                    dto.getStationId(),
                    dto.getVehiculeId(),
                    dto.getQuantite(),
                    dto.getChauffeurId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(new DemandeResponse(demande));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Erreur lors de la création de la demande : " + e.getMessage());
        }
    }

    @Operation(summary = "Lister les demandes de carburant")
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
            Long entrepriseId = utilisateur.getEntreprise().getId();
            return ResponseEntity.ok(
                    demandeService.getDemandesParEntreprise(entrepriseId)
                            .stream()
                            .map(DemandeResponse::new)
                            .toList());

        } else if (utilisateur.getRole() == Role.DEMANDEUR) {
            return ResponseEntity.ok(
                    demandeService.getDemandesParDemandeur(utilisateur.getId())
                            .stream()
                            .map(DemandeResponse::new)
                            .toList());
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("❌ Accès refusé");
    }
}
