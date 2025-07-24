package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.CarburantRepository;
import com.fasocarbu.fasocarbu.repositories.DemandeRepository;
import com.fasocarbu.fasocarbu.repositories.StationRepository;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*")
public class DemandeController {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CarburantRepository carburantRepository;
@PostMapping
public ResponseEntity<?> createDemande(@RequestBody Demande demande, Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(userDetails.getUsername());

    if (utilisateurOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Utilisateur non trouvé");
    }

    Utilisateur utilisateur = utilisateurOpt.get();

    if (!(utilisateur instanceof Chauffeur)) {
        return ResponseEntity.badRequest().body("Seuls les chauffeurs peuvent créer des demandes");
    }

    Chauffeur chauffeur = (Chauffeur) utilisateur;
    demande.setChauffeur(chauffeur);
    demande.setDateDemande(LocalDate.now());

    // Vérification de la station
    if (demande.getStation() != null && demande.getStation().getId() != 0L) {
        Station station = stationRepository.findById(demande.getStation().getId()).orElse(null);
        demande.setStation(station);
    } else {
        demande.setStation(null); // Optionnel selon tes besoins
    }

    // Vérification du carburant
    if (demande.getCarburant() != null && demande.getCarburant().getId() != null && demande.getCarburant().getId() != 0L) {
        Carburant carburant = carburantRepository.findById(demande.getCarburant().getId()).orElse(null);
        demande.setCarburant(carburant);
    } else {
        demande.setCarburant(null); // Optionnel selon tes besoins
    }

    Demande savedDemande = demandeRepository.save(demande);
    return ResponseEntity.ok(savedDemande);
}

}
