package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.CarburantRepository;
import com.fasocarbu.fasocarbu.repositories.DemandeRepository;
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

    // ❌ Plus besoin de chercher une station
    // ✅ Carburant (optionnel selon ton backend)
    if (demande.getCarburant() != null && demande.getCarburant().getId() != null && demande.getCarburant().getId() != 0L) {
        Carburant carburant = carburantRepository.findById(demande.getCarburant().getId()).orElse(null);
        demande.setCarburant(carburant);
    } else {
        demande.setCarburant(null);
    }

    Demande savedDemande = demandeRepository.save(demande);
    return ResponseEntity.ok(savedDemande);
}


}
