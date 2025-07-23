package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.fasocarbu.fasocarbu.enums.Role;


import java.util.List;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private UtilisateurService utilisateurService;

    /**
     * Créer une nouvelle demande (réservée aux chauffeurs)
     */
    @PostMapping
    @PreAuthorize("hasRole('CHAUFFEUR')")
    public ResponseEntity<?> createDemande(@RequestBody Demande demande,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Utilisateur user = utilisateurService.findByEmail(userDetails.getUsername()).orElse(null);
            if (user.getRole() == Role.CHAUFFEUR) {
                return ResponseEntity.status(403).body("Seuls les chauffeurs peuvent créer des demandes");
            }

            String nomComplet = user.getPrenom() + " " + user.getNom();
            demande.setDemandeur(nomComplet);
            Demande saved = demandeService.saveDemande(demande);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la création de la demande : " + e.getMessage());
        }
    }

    /**
     * Liste des demandes (accessible à tous les rôles)
     */
    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }

    /**
     * Obtenir les demandes du chauffeur connecté
     */
    @GetMapping("/mes-demandes")
    @PreAuthorize("hasRole('CHAUFFEUR')")
    public ResponseEntity<?> getDemandesDuChauffeur(@AuthenticationPrincipal UserDetails userDetails) {
        Utilisateur user = utilisateurService.findByEmail(userDetails.getUsername()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur non trouvé");
        }

        String nomComplet = user.getPrenom() + " " + user.getNom();
        List<Demande> demandes = demandeService.getDemandesParDemandeur(nomComplet);
        return ResponseEntity.ok(demandes);
    }
}
