package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.*;
import com.fasocarbu.fasocarbu.models.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GestionnaireService {

    Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire);

    Gestionnaire obtenirGestionnaire(UUID id);

    List<Gestionnaire> obtenirTousLesGestionnaires();

    Gestionnaire modifierGestionnaire(UUID id, Gestionnaire gestionnaire);

    void supprimerGestionnaire(UUID id);

    Gestionnaire ajouterGestionnaireAvecEntreprise(GestionnaireAvecEntrepriseRequest request);

    Long getEntrepriseIdFromUser(UUID userId);

    Chauffeur creerChauffeur(Chauffeur chauffeur);

    List<Chauffeur> obtenirChauffeursParEntreprise(Long entrepriseId);

    Vehicule creerVehicule(Vehicule vehicule);

    Vehicule definirQuotaPourVehicule(Long vehiculeId, double quota);

    List<Vehicule> obtenirVehiculesParEntreprise(Long entrepriseId);

    Station creerStationAvecAdmin(StationAvecAdminRequest request, UUID gestionnaireId);

    List<Station> obtenirToutesLesStations();

    List<Station> obtenirStationsParEntreprise(Long entrepriseId);

    List<Gestionnaire> obtenirGestionnairesParEntreprise(Long entrepriseId);

    Demandeur creerDemandeur(Demandeur demandeur);

    Demande creerDemandePourEntreprise(DemandeRequest request);

    Ticket validerDemandeEtGenererTicket(Long id);

    Demande rejeterDemande(Long id, String motif);

    List<Demande> getDemandesParStatut(String statut);

    List<Demande> obtenirDemandesParEntreprise(Long entrepriseId);

    List<TicketDTO> getTicketsParChauffeur(UUID chauffeurId);

    List<TicketDTO> getTicketsParChauffeurEtDates(UUID chauffeurId, LocalDateTime debut, LocalDateTime fin);

    List<TicketDTO> obtenirTicketsParEntreprise(Long entrepriseId);

    List<TicketDTO> getTicketsParChauffeurEtEntreprise(UUID chauffeurId, Long entrepriseId);

    List<TicketDTO> getTicketsParChauffeurEtDatesEtEntreprise(UUID chauffeurId, LocalDateTime debut, LocalDateTime fin,
            Long entrepriseId);

    Ticket validerDemandeEtGenererTicketParEntreprise(Long demandeId, Long entrepriseId);

    Demande rejeterDemandeParEntreprise(Long demandeId, String motif, Long entrepriseId);

    List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId);

    ResponseEntity<Resource> exporterRapportConsommation();

    Resource exporterRapportConsommationParEntreprise(Long entrepriseId);
}
