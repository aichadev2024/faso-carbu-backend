package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import com.fasocarbu.fasocarbu.models.Vehicule;
import com.fasocarbu.fasocarbu.models.Station;
import com.fasocarbu.fasocarbu.models.Ticket;
import com.fasocarbu.fasocarbu.models.Demande;

import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
import com.fasocarbu.fasocarbu.dtos.MotifRejetRequest;


import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    private final GestionnaireService service;

    public GestionnaireController(GestionnaireService service) {
        this.service = service;
    }

    // CRUD Gestionnaire
    @PostMapping
    public ResponseEntity<Gestionnaire> ajouter(@Valid @RequestBody Gestionnaire gestionnaire) {
        Gestionnaire g = service.ajouterGestionnaire(gestionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(g);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gestionnaire> obtenir(@PathVariable UUID id) {
        Gestionnaire g = service.obtenirGestionnaire(id);
        return ResponseEntity.ok(g);
    }

    @GetMapping
    public ResponseEntity<List<Gestionnaire>> obtenirTous() {
        List<Gestionnaire> gestionnaires = service.obtenirTousLesGestionnaires();
        return ResponseEntity.ok(gestionnaires);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gestionnaire> modifier(@PathVariable UUID id, @Valid @RequestBody Gestionnaire gestionnaire) {
        Gestionnaire g = service.modifierGestionnaire(id, gestionnaire);
        return ResponseEntity.ok(g);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable UUID id) {
        service.supprimerGestionnaire(id);
        return ResponseEntity.noContent().build();
    }

    // Créer un chauffeur
    @PostMapping("/chauffeurs")
    public ResponseEntity<Chauffeur> creerChauffeur(@Valid @RequestBody Chauffeur chauffeur) {
        Chauffeur c = service.creerChauffeur(chauffeur);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    // Créer un véhicule
    @PostMapping("/vehicules")
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule) {
        Vehicule v = service.creerVehicule(vehicule);
        return ResponseEntity.status(HttpStatus.CREATED).body(v);
    }

    // Créer une station avec admin
    @PostMapping("/stations")
    public ResponseEntity<Station> creerStation(@Valid @RequestBody StationAvecAdminRequest request) {
        Station s = service.creerStationAvecAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    // Créer une demande à la place du chauffeur
    @PostMapping("/demandes")
    public ResponseEntity<Demande> creerDemande(@Valid @RequestBody DemandeRequest request) {
        Demande d = service.creerDemandePourEntreprise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    // Valider une demande et générer un ticket
    @PostMapping("/demandes/{id}/valider")
    public ResponseEntity<Ticket> validerDemande(@PathVariable Long id) {
        Ticket t = service.validerDemandeEtGenererTicket(id);
        return ResponseEntity.ok(t);
    }

    // Rejeter une demande
    @PostMapping("/demandes/{id}/rejeter")
    public ResponseEntity<Demande> rejeterDemande(@PathVariable Long id, @Valid @RequestBody MotifRejetRequest motif) {
        Demande d = service.rejeterDemande(id, motif.getMotif());
        return ResponseEntity.ok(d);
    }

    // Exporter rapport consommation
    @GetMapping("/rapport/consommation")
    public ResponseEntity<Resource> exporterRapport() {
        return service.exporterRapportConsommation();
    }
}
