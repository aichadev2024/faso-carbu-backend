package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.VehiculeDTO;
import com.fasocarbu.fasocarbu.dtos.VehiculeRequest;
import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.models.Vehicule;
import com.fasocarbu.fasocarbu.services.interfaces.VehiculeService;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    @Autowired
    private CarburantService carburantService;

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public VehiculeDTO ajouterVehicule(
            @Valid @RequestBody VehiculeRequest vehiculeRequest,
            Authentication authentication) {

        // récupérer le gestionnaire connecté
        String email = authentication.getName();
        Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail(email);

        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur connecté introuvable");
        }

        // Vérification du carburant
        Carburant carburant = carburantService.getCarburantById(vehiculeRequest.getCarburantId());
        if (carburant == null) {
            throw new IllegalArgumentException(
                    "Carburant introuvable pour l'id : " + vehiculeRequest.getCarburantId());
        }

        // Création du véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setMarque(vehiculeRequest.getMarque());
        vehicule.setModele(vehiculeRequest.getModele());
        vehicule.setImmatriculation(vehiculeRequest.getImmatriculation());
        vehicule.setQuotaCarburant(vehiculeRequest.getQuotaCarburant());
        vehicule.setCarburant(carburant);
        vehicule.setUtilisateur(utilisateur); // 👉 assigné automatiquement au gestionnaire connecté

        Vehicule savedVehicule = vehiculeService.enregistrerVehicule(vehicule);

        return new VehiculeDTO(savedVehicule);
    }

    // Récupérer tous les véhicules
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

    // Récupérer un véhicule par ID
    @GetMapping("/{id}")
    public VehiculeDTO getVehicule(@PathVariable Long id) {
        Vehicule vehicule = vehiculeService.getVehiculeById(id);
        return new VehiculeDTO(vehicule);
    }

    // Supprimer un véhicule
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public void supprimerVehicule(@PathVariable Long id) {
        vehiculeService.supprimerVehicule(id);
    }
}
