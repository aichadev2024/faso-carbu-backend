package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
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
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    private final GestionnaireService service;
    @Autowired
    private GestionnaireService gestionnaireService;

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
    public ResponseEntity<List<Gestionnaire>> obtenirTous(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirGestionnairesParEntreprise(entrepriseId));
    }

    @PostMapping
    public ResponseEntity<Gestionnaire> ajouter(@Valid @RequestBody Gestionnaire gestionnaire,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        gestionnaire.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouterGestionnaire(gestionnaire));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> modifier(@PathVariable UUID id,
            @Valid @RequestBody Gestionnaire gestionnaire,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        gestionnaire.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
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
    public ResponseEntity<Chauffeur> creerChauffeur(@Valid @RequestBody Chauffeur chauffeur,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chauffeur.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
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
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        vehicule.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
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
        request.setEntrepriseId(userDetails.getEntrepriseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerStationAvecAdmin(request, gestionnaireId));
    }

    @GetMapping("/stations")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Station>> getStations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirStationsParEntreprise(entrepriseId));
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
    public ResponseEntity<?> validerDemande(
            @PathVariable Long id,
            @RequestParam UUID chauffeurId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();

        try {
            Ticket ticket = service.validerDemandeEtGenererTicketParEntreprise(id, entrepriseId, chauffeurId);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/demandes/{id}/rejeter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> rejeterDemande(
            @PathVariable Long id,
            @RequestBody Map<String, String> body, // récupère le JSON du body
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();
        String motif = body.get("motif"); // récupère le motif depuis le body
        if (motif == null || motif.isEmpty()) {
            return ResponseEntity.badRequest().body("Le champ 'motif' est obligatoire");
        }

        Demande demande;
        try {
            demande = service.rejeterDemandeParEntreprise(id, motif, entrepriseId);
        } catch (RuntimeException e) { // <-- uniquement RuntimeException
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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

    @GetMapping("/export/consommation/{entrepriseId}")
    public ResponseEntity<Resource> exporterRapportParEntreprise(@PathVariable Long entrepriseId) {
        Resource file = gestionnaireService.exporterRapportConsommationParEntreprise(entrepriseId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"rapport.xlsx\"")
                .body(file);
    }

    @GetMapping("/rapport/tickets")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTicketsParChauffeur(@RequestParam UUID chauffeurId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.getTicketsParChauffeurEtEntreprise(chauffeurId, entrepriseId));
    }

    @GetMapping("/rapport/tickets/filtre")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTicketsParChauffeurEtDates(
            @RequestParam UUID chauffeurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity
                .ok(service.getTicketsParChauffeurEtDatesEtEntreprise(chauffeurId, dateDebut, dateFin, entrepriseId));
    }
}
