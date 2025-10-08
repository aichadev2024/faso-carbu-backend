package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.*;
import com.fasocarbu.fasocarbu.models.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GestionnaireService {

    // Gestionnaires
    Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire);

    Gestionnaire obtenirGestionnaire(UUID id);

    List<Gestionnaire> obtenirTousLesGestionnaires();

    Gestionnaire modifierGestionnaire(UUID id, Gestionnaire gestionnaire);

    void supprimerGestionnaire(UUID id);

    Gestionnaire ajouterGestionnaireAvecEntreprise(GestionnaireAvecEntrepriseRequest request);

    // Récupération de l’entreprise liée à l’utilisateur connecté
    Long getEntrepriseIdFromUser(UUID userId);

    // Chauffeurs
    Chauffeur creerChauffeur(Chauffeur chauffeur);

    List<Chauffeur> obtenirChauffeursParEntreprise(Long entrepriseId);

    // Véhicules
    Vehicule creerVehicule(Vehicule vehicule);

    Vehicule definirQuotaPourVehicule(Long vehiculeId, double quota);

    List<Vehicule> obtenirVehiculesParEntreprise(Long entrepriseId);

    // Stations
    List<Station> obtenirToutesLesStations();

    Station creerStationAvecAdmin(StationAvecAdminRequest request, UUID gestionnaireId);

    List<Station> obtenirStationsParEntreprise(Long entrepriseId);

    // Demandes
    Demande creerDemandePourEntreprise(DemandeRequest request);

    Ticket validerDemandeEtGenererTicket(Long id);

    Demande rejeterDemande(Long id, String motif);

    List<Demande> getDemandesParStatut(String statut);

    List<Demande> obtenirDemandesParEntreprise(Long entrepriseId);

    // Demandeurs
    Demandeur creerDemandeur(Demandeur demandeur);

    // Tickets
    List<TicketDTO> getTicketsParChauffeur(UUID chauffeurId);

    List<TicketDTO> getTicketsParChauffeurEtDates(UUID chauffeurId, LocalDateTime debut, LocalDateTime fin);

    List<TicketDTO> obtenirTicketsParEntreprise(Long entrepriseId);

    // Consommation & rapports
    List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId);

    ResponseEntity<Resource> exporterRapportConsommation();
}
