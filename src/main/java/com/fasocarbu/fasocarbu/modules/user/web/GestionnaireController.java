package com.fasocarbu.fasocarbu.modules.user.web;

import com.fasocarbu.fasocarbu.core.security.services.UserDetailsImpl;
import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import com.fasocarbu.fasocarbu.modules.user.domain.Gestionnaire;
import com.fasocarbu.fasocarbu.modules.user.dto.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.modules.user.service.GestionnaireService;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "Gestionnaire", description = "Endpoints de gestion d'entreprise, véhicules, stations, demandes et rapports")
@RestController
@RequestMapping("/api/gestionnaires")
public class GestionnaireController {

    private final GestionnaireService service;

    public GestionnaireController(GestionnaireService service) {
        this.service = service;
    }

    @Operation(summary = "Lister les gestionnaires de l'entreprise")
    @GetMapping
    public ResponseEntity<List<Gestionnaire>> obtenirTous(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirGestionnairesParEntreprise(entrepriseId));
    }

    @Operation(summary = "Créer un chauffeur")
    @PostMapping("/chauffeurs")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Chauffeur> creerChauffeur(@Valid @RequestBody Chauffeur chauffeur,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chauffeur.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerChauffeur(chauffeur));
    }

    @Operation(summary = "Lister les chauffeurs de l'entreprise")
    @GetMapping("/chauffeurs")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Chauffeur>> getChauffeurs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirChauffeursParEntreprise(entrepriseId));
    }

    @Operation(summary = "Créer un véhicule")
    @PostMapping("/vehicules")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Vehicule> creerVehicule(@Valid @RequestBody Vehicule vehicule,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        vehicule.setEntreprise(new Entreprise(userDetails.getEntrepriseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creerVehicule(vehicule));
    }

    @Operation(summary = "Lister les véhicules de l'entreprise")
    @GetMapping("/vehicules")
    @PreAuthorize("hasRole('GESTIONNAIRE','DEMANDEUR')")
    public ResponseEntity<List<Vehicule>> getVehicules(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirVehiculesParEntreprise(entrepriseId));
    }

    @Operation(summary = "Créer une station avec un administrateur")
    @PostMapping("/stations")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<Station> creerStationAvecAdmin(@RequestBody StationAvecAdminRequest request,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UUID gestionnaireId = userDetails.getId();
        request.setEntrepriseId(userDetails.getEntrepriseId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.creerStationAvecAdmin(request, gestionnaireId));
    }

    @Operation(summary = "Lister les stations de l'entreprise")
    @GetMapping("/stations")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE', 'DEMANDEUR')")
    public ResponseEntity<List<Station>> getStations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirStationsParEntreprise(entrepriseId));
    }

    @Operation(summary = "Lister les demandes de l'entreprise")
    @GetMapping("/demandes")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<Demande>> getDemandes(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirDemandesParEntreprise(entrepriseId));
    }

    @Operation(summary = "Valider une demande et générer un ticket")
    @PostMapping("/demandes/{id}/valider")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> validerDemande(
            @PathVariable Long id,
            @RequestParam(required = false) UUID chauffeurId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long entrepriseId = userDetails.getEntrepriseId();

        try {
            Ticket ticket = service.validerDemandeEtGenererTicketParEntreprise(id, entrepriseId, chauffeurId);
            return ResponseEntity.ok(ticket);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la validation de la demande : " + e.getMessage());
        }
    }

    @Operation(summary = "Rejeter une demande")
    @PostMapping("/demandes/{id}/rejeter")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<?> rejeterDemande(@PathVariable Long id,
                                            @RequestBody Map<String, String> body,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        String motif = body.get("motif");

        if (motif == null || motif.isEmpty()) {
            return ResponseEntity.badRequest().body("Le champ 'motif' est obligatoire");
        }

        try {
            Demande demande = service.rejeterDemandeParEntreprise(id, motif, entrepriseId);
            return ResponseEntity.ok(demande);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Lister les tickets de l'entreprise")
    @GetMapping("/tickets")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTickets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.obtenirTicketsParEntreprise(entrepriseId));
    }

    @Operation(summary = "Exporter le rapport de consommation (Excel)")
    @GetMapping("/export/consommation/{entrepriseId}")
    public ResponseEntity<Resource> exporterRapportParEntreprise(@PathVariable Long entrepriseId) {
        Resource file = service.exporterRapportConsommationParEntreprise(entrepriseId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"rapport.xlsx\"")
                .body(file);
    }

    @Operation(summary = "Rapport des tickets par chauffeur")
    @GetMapping("/rapport/tickets")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<List<TicketDTO>> getTicketsParChauffeur(@RequestParam UUID chauffeurId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long entrepriseId = userDetails.getEntrepriseId();
        return ResponseEntity.ok(service.getTicketsParChauffeurEtEntreprise(chauffeurId, entrepriseId));
    }

    @Operation(summary = "Rapport des tickets par chauffeur et plage de dates")
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
