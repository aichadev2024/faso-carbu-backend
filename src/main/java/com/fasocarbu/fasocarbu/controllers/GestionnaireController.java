package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
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

    @Autowired
    private GestionnaireService gestionnaireService;
    private final GestionnaireService service;

    public GestionnaireController(GestionnaireService service) {
        this.service = service;
    }

    // ------------------- Gestionnaires -------------------

    // Récupérer un gestionnaire par ID (mettre {id} sous un path spécifique)
    @GetMapping("/id/{id}")
    public ResponseEntity<Gestionnaire> obtenir(@PathVariable UUID id) {
        Gestionnaire g = service.obtenirGestionnaire(id);
        return ResponseEntity.ok(g);
    }

    // Liste tous les gestionnaires
    @GetMapping
    public ResponseEntity<List<Gestionnaire>> obtenirTous() {
        List<Gestionnaire> gestionnaires = service.obtenirTousLesGestionnaires();
        return ResponseEntity.ok(gestionnaires);
    }

    @PostMapping
    public ResponseEntity<Gestionnaire> ajouter(@Valid @RequestBody Gestionnaire gestionnaire) {
        Gestionnaire g = service.ajouterGestionnaire(gestionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(g);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Gestionnaire> modifier(@PathVariable UUID id, @Valid @RequestBody Gestionnaire gestionnaire) {
        Gestionnaire g = service.modifierGestionnaire(id, gestionnaire);
        return ResponseEntity.ok(g);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable UUID id) {
        service.supprimerGestionnaire(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- Chauffeurs -------------------
    @PostMapping("/chauffeurs")
    public ResponseEntity<Chauffeur> creerChauffeur(@Valid @RequestBody Chauffeur chauffeur) {
        Chauffeur c = service.creerChauffeur(chauffeur);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    // ------------------- Véhicules -------------------
    @PostMapping("/vehicules")
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule) {
        Vehicule v = service.creerVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(v);
    }

    // ------------------- Stations -------------------
    @PostMapping("/stations")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Station> creerStationAvecAdmin(
            @RequestBody StationAvecAdminRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UUID gestionnaireId = userDetails.getId(); // id du gestionnaire connecté
        Station station = gestionnaireService.creerStationAvecAdmin(request, gestionnaireId);

        return ResponseEntity.status(HttpStatus.CREATED).body(station);
    }

    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getStations() {
        List<Station> stations = service.obtenirToutesLesStations();
        return ResponseEntity.ok(stations);
    }

    // ------------------- Demandes -------------------
    @PostMapping("/demandes")
    public ResponseEntity<Demande> creerDemande(@Valid @RequestBody DemandeRequest request) {
        Demande d = service.creerDemandePourEntreprise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    @PostMapping("/demandes/{id}/valider")
    public ResponseEntity<Ticket> validerDemande(@PathVariable Long id) {
        Ticket t = service.validerDemandeEtGenererTicket(id);
        return ResponseEntity.ok(t);
    }

    @PostMapping("/demandes/{id}/rejeter")
    public ResponseEntity<Demande> rejeterDemande(@PathVariable Long id, @Valid @RequestBody MotifRejetRequest motif) {
        Demande d = service.rejeterDemande(id, motif.getMotif());
        return ResponseEntity.ok(d);
    }

    // ------------------- Rapports -------------------
    @GetMapping("/rapport/consommation")
    public ResponseEntity<Resource> exporterRapport() {
        return service.exporterRapportConsommation();
    }
}
