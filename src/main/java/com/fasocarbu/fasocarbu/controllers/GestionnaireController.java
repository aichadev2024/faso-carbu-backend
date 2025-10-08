package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.dtos.MotifRejetRequest;
import com.fasocarbu.fasocarbu.dtos.TicketDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

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
    public ResponseEntity<?> obtenir(@PathVariable UUID id) {
        Gestionnaire gestionnaire = service.obtenirGestionnaire(id);
        if (gestionnaire == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gestionnaire introuvable");
        }
        return ResponseEntity.ok(gestionnaire);
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
    public ResponseEntity<?> modifier(@PathVariable UUID id, @Valid @RequestBody Gestionnaire gestionnaire) {
        Gestionnaire updated = service.modifierGestionnaire(id, gestionnaire);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gestionnaire introuvable pour mise à jour");
        }
        return ResponseEntity.ok(updated);
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

    @GetMapping("/chauffeurs")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Chauffeur>> getChauffeurs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirChauffeursParEntreprise(entrepriseId));
    }

    // ------------------- Véhicules -------------------
    @PostMapping("/vehicules")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerVehicule(vehicule));
    }

    @GetMapping("/vehicules")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Vehicule>> getVehicules(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirVehiculesParEntreprise(entrepriseId));
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
    public ResponseEntity<List<Station>> getStations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        List<Station> stations = service.obtenirStationsParEntreprise(entrepriseId);
        return ResponseEntity.ok(stations);
    }

    // ------------------- Demandes -------------------
    @GetMapping("/demandes")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Demande>> getDemandes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirDemandesParEntreprise(entrepriseId));
    }

    @PostMapping("/demandes/{id}/valider")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> validerDemande(@PathVariable Long id) {
        Ticket ticket = service.validerDemandeEtGenererTicket(id);
        if (ticket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande introuvable ou déjà traitée");
        }
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/demandes/{id}/rejeter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> rejeterDemande(@PathVariable Long id, @Valid @RequestBody MotifRejetRequest motif) {
        Demande demande = service.rejeterDemande(id, motif.getMotif());
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demande introuvable ou déjà traitée");
        }
        return ResponseEntity.ok(demande);
    }

    // ------------------- Tickets -------------------
    @GetMapping("/tickets")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirTicketsParEntreprise(entrepriseId));
    }

    // ------------------- Rapports -------------------
    @GetMapping("/rapport/consommation")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Resource> exporterRapport() {
        return service.exporterRapportConsommation();
    }

    @GetMapping("/rapport/tickets")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTicketsParChauffeur(@RequestParam UUID chauffeurId) {
        return ResponseEntity.ok(service.getTicketsParChauffeur(chauffeurId));
    }

    @GetMapping("/rapport/tickets/filtre")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTicketsParChauffeurEtDates(
            @RequestParam UUID chauffeurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin) {
        return ResponseEntity.ok(service.getTicketsParChauffeurEtDates(chauffeurId, dateDebut, dateFin));
    }
}
