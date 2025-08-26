package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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

    // ===================== CREER DEMANDE =====================
    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<?> createDemande(@RequestBody Demande demande, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // ✅ Seul un gestionnaire ou un demandeur peut créer une demande
        if (!(utilisateur instanceof Gestionnaire) && !(utilisateur instanceof Demandeur)) {
            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à créer une demande");
        }

        // Associer demandeur + date
        demande.setDemandeur(utilisateur);
        demande.setDateDemande(LocalDateTime.now());

        // Récupérer entités liées
        if (demande.getStation() != null && demande.getStation().getId() != 0L) {
            stationRepository.findById(demande.getStation().getId()).ifPresent(demande::setStation);
        }

        if (demande.getCarburant() != null && demande.getCarburant().getId() != null) {
            carburantRepository.findById(demande.getCarburant().getId()).ifPresent(demande::setCarburant);
        }

        if (demande.getVehicule() != null && demande.getVehicule().getId() != 0L) {
            vehiculeRepository.findById(demande.getVehicule().getId()).ifPresent(demande::setVehicule);
        }

        // 🔥 Sauvegarde
        Demande savedDemande = demandeRepository.save(demande);

        // ✅ Notification aux gestionnaires
        notificationService.notifierGestionnairesNouvelleDemande(savedDemande);

        return ResponseEntity.ok(savedDemande);
    }

    // ===================== LISTE DES DEMANDES =====================
    @GetMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<List<Demande>> getDemandes(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // 🔹 Si gestionnaire → retourne toutes les demandes
        if (utilisateur instanceof Gestionnaire) {
            return ResponseEntity.ok(demandeRepository.findAll());
        }

        // 🔹 Si demandeur → retourne seulement ses demandes
        if (utilisateur instanceof Demandeur) {
            return ResponseEntity.ok(demandeRepository.findByDemandeur(utilisateur));
        }

        return ResponseEntity.status(403).build();
    }
}
