package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.dtos.MotifRejetRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    private final GestionnaireService service;

    @Autowired
    public GestionnaireController(GestionnaireService service) {
        this.service = service;
    }

    // ------------------- Gestionnaires -------------------
    @GetMapping("/id/{id}")
    public ResponseEntity<Gestionnaire> obtenir(@PathVariable UUID id) {
        return ResponseEntity.ok(service.obtenirGestionnaire(id));
    }

    @GetMapping
    public ResponseEntity<List<Gestionnaire>> obtenirTous() {
        return ResponseEntity.ok(service.obtenirTousLesGestionnaires());
    }

    @PostMapping
    public ResponseEntity<Gestionnaire> ajouter(@Valid @RequestBody Gestionnaire gestionnaire) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouterGestionnaire(gestionnaire));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Gestionnaire> modifier(@PathVariable UUID id, @Valid @RequestBody Gestionnaire gestionnaire) {
        return ResponseEntity.ok(service.modifierGestionnaire(id, gestionnaire));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable UUID id) {
        service.supprimerGestionnaire(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- Chauffeurs -------------------
    @PostMapping("/chauffeurs")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Chauffeur> creerChauffeur(@Valid @RequestBody Chauffeur chauffeur) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerChauffeur(chauffeur));
    }

    // ------------------- Véhicules -------------------
    @PostMapping("/vehicules")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerVehicule(vehicule));
    }

    // ------------------- Stations -------------------
    @PostMapping("/stations")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Station> creerStationAvecAdmin(
            @RequestBody StationAvecAdminRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID gestionnaireId = userDetails.getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerStationAvecAdmin(request, gestionnaireId));
    }

    @GetMapping("/stations")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Station>> getStations() {
        return ResponseEntity.ok(service.obtenirToutesLesStations());
    }

    // ------------------- Validation / Rejet des demandes -------------------
    @PostMapping("/demandes/{id}/valider")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Ticket> validerDemande(@PathVariable Long id) {
        return ResponseEntity.ok(service.validerDemandeEtGenererTicket(id));
    }

    @PostMapping("/demandes/{id}/rejeter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Demande> rejeterDemande(@PathVariable Long id, @Valid @RequestBody MotifRejetRequest motif) {
        return ResponseEntity.ok(service.rejeterDemande(id, motif.getMotif()));
    }

    // ------------------- Rapports -------------------
    @GetMapping("/rapport/consommation")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Resource> exporterRapport() {
        return service.exporterRapportConsommation();
    }
}
