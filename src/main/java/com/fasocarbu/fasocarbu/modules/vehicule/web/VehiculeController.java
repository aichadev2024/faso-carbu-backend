package com.fasocarbu.fasocarbu.modules.vehicule.web;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.service.CarburantService;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.service.UtilisateurService;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import com.fasocarbu.fasocarbu.modules.vehicule.dto.VehiculeDTO;
import com.fasocarbu.fasocarbu.modules.vehicule.dto.VehiculeRequest;
import com.fasocarbu.fasocarbu.modules.vehicule.service.VehiculeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Véhicules", description = "Endpoints de gestion de la flotte de véhicules")
@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    private final VehiculeService vehiculeService;
    private final CarburantService carburantService;
    private final UtilisateurService utilisateurService;

    public VehiculeController(VehiculeService vehiculeService,
                              CarburantService carburantService,
                              UtilisateurService utilisateurService) {
        this.vehiculeService = vehiculeService;
        this.carburantService = carburantService;
        this.utilisateurService = utilisateurService;
    }

    @Operation(summary = "Ajouter un véhicule (Gestionnaire)")
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public VehiculeDTO ajouterVehicule(
            @Valid @RequestBody VehiculeRequest vehiculeRequest,
            Authentication authentication) {

        String email = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);

        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur connecté introuvable");
        }

        Carburant carburant = carburantService.getCarburantById(vehiculeRequest.getCarburantId());
        if (carburant == null) {
            throw new IllegalArgumentException(
                    "Carburant introuvable pour l'id : " + vehiculeRequest.getCarburantId());
        }

        Vehicule vehicule = new Vehicule();
        vehicule.setMarque(vehiculeRequest.getMarque());
        vehicule.setModele(vehiculeRequest.getModele());
        vehicule.setImmatriculation(vehiculeRequest.getImmatriculation());
        vehicule.setQuotaCarburant(vehiculeRequest.getQuotaCarburant());
        vehicule.setCarburant(carburant);
        vehicule.setUtilisateur(utilisateur);

        Vehicule savedVehicule = vehiculeService.enregistrerVehicule(vehicule);

        return new VehiculeDTO(savedVehicule);
    }

    @Operation(summary = "Lister tous les véhicules d'une entreprise")
    @GetMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public List<VehiculeDTO> getAllVehicules(Authentication authentication) {
        String email = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);

        if (utilisateur == null || utilisateur.getEntreprise() == null) {
            throw new IllegalArgumentException("Entreprise non trouvée pour l'utilisateur connecté");
        }

        Long entrepriseId = utilisateur.getEntreprise().getId();

        return vehiculeService.getVehiculesParEntreprise(entrepriseId)
                .stream()
                .map(VehiculeDTO::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Obtenir un véhicule par ID")
    @GetMapping("/{id}")
    public VehiculeDTO getVehicule(@PathVariable Long id) {
        Vehicule vehicule = vehiculeService.getVehiculeById(id);
        return new VehiculeDTO(vehicule);
    }

    @Operation(summary = "Supprimer un véhicule")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerVehicule(id);
    }
}
