package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CarburantRepository carburantRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createDemande(@RequestBody Demande demande, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // ❌ Les chauffeurs ne peuvent PAS créer de demandes
        if (utilisateur instanceof Chauffeur) {
            return ResponseEntity.badRequest().body("Les chauffeurs ne sont pas autorisés à créer des demandes");
        }

        // ✅ Associer l'utilisateur (employé ou gestionnaire) à la demande
        demande.setDemandeur(utilisateur);
        demande.setDateDemande(LocalDateTime.now());

        // Gestion station
        Station stationFromRequest = demande.getStation();
        Station station = null;
        if (stationFromRequest != null) {
            if (stationFromRequest.getId() != 0L) {
                station = stationRepository.findById(stationFromRequest.getId()).orElse(null);
            } else {
                Optional<Station> stationOpt = stationRepository.findByNomAndVilleAndAdresse(
                        stationFromRequest.getNom(),
                        stationFromRequest.getVille(),
                        stationFromRequest.getAdresse()
                );
                station = stationOpt.orElseGet(() -> stationRepository.save(stationFromRequest));
            }
        }
        demande.setStation(station);

        // Gestion carburant
        if (demande.getCarburant() != null && demande.getCarburant().getId() != null) {
            Carburant carburant = carburantRepository.findById(demande.getCarburant().getId()).orElse(null);
            demande.setCarburant(carburant);
        } else {
            demande.setCarburant(null);
        }

        // Gestion véhicule
        Vehicule vehiculeFromRequest = demande.getVehicule();
        Vehicule vehicule = null;
        if (vehiculeFromRequest != null) {
            if (vehiculeFromRequest.getId() != 0L) {
                vehicule = vehiculeRepository.findById(vehiculeFromRequest.getId()).orElse(null);
            } else {
                Optional<Vehicule> vehiculeOpt = vehiculeRepository.findByImmatriculation(vehiculeFromRequest.getImmatriculation());
                vehicule = vehiculeOpt.orElseGet(() -> vehiculeRepository.save(vehiculeFromRequest));
            }
        }
        demande.setVehicule(vehicule);

        // 🔥 Sauvegarde de la demande
        Demande savedDemande = demandeRepository.save(demande);

        // ✅ Notification aux gestionnaires
        notificationService.notifierGestionnairesNouvelleDemande(savedDemande);

        return ResponseEntity.ok(savedDemande);
    }
}
