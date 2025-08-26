package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
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
    public ResponseEntity<?> createDemande(@RequestBody DemandeRequest dto,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier que le créateur est bien autorisé
        if (!(utilisateur instanceof Gestionnaire) && !(utilisateur instanceof Demandeur)) {
            return ResponseEntity.status(403).body("Vous n'êtes pas autorisé à créer une demande");
        }

        // 🔹 Créer une nouvelle demande
        Demande demande = new Demande();
        demande.setDateDemande(LocalDateTime.now());
        demande.setQuantite(dto.getQuantite());

        // ✅ Récupérer le demandeur
        if (dto.getDemandeurId() != null) {
            utilisateurRepository.findById(dto.getDemandeurId()).ifPresent(demande::setDemandeur);
        } else {
            // Si pas de demandeur → on met l'utilisateur courant comme demandeur
            demande.setDemandeur(utilisateur);
        }

        // ✅ Récupérer carburant
        if (dto.getCarburantId() != null) {
            carburantRepository.findById(dto.getCarburantId()).ifPresent(demande::setCarburant);
        }

        // ✅ Récupérer station
        if (dto.getStationId() != null) {
            stationRepository.findById(dto.getStationId()).ifPresent(demande::setStation);
        }

        // ✅ Récupérer véhicule
        if (dto.getVehiculeId() != null) {
            vehiculeRepository.findById(dto.getVehiculeId()).ifPresent(demande::setVehicule);
        }

        // 🔥 Sauvegarde
        Demande savedDemande = demandeRepository.save(demande);

        // ✅ Notifier gestionnaires
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
