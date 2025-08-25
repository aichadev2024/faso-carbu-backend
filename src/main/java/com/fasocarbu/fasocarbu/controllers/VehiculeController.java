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

import java.util.List;
import java.util.UUID;
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

    // Ajouter un véhicule
    @PostMapping("/ajouter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public VehiculeDTO ajouterVehicule(@RequestBody VehiculeRequest vehiculeRequest) {

        Carburant carburant = carburantService.getCarburantById(vehiculeRequest.getCarburantId());
        if (carburant == null) {
            throw new IllegalArgumentException(
                    "Carburant introuvable pour l'id : " + vehiculeRequest.getCarburantId());
        }
        if (carburant.getNom() == null) {
            carburant.setNom("Carburant inconnu");
        }

        UUID utilisateurId = UUID.fromString(vehiculeRequest.getUtilisateurId());
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(utilisateurId);
        if (utilisateur == null) {
            throw new IllegalArgumentException(
                    "Utilisateur introuvable pour l'id : " + vehiculeRequest.getUtilisateurId());
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

    // Récupérer tous les véhicules
    @GetMapping
    public List<VehiculeDTO> getAllVehicules() {
        return vehiculeService.getAllVehicules()
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
