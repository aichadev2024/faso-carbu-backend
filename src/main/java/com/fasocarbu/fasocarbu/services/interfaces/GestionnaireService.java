package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
import com.fasocarbu.fasocarbu.dtos.GestionnaireAvecEntrepriseRequest;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.models.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

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

    Chauffeur creerChauffeur(Chauffeur chauffeur);

    Vehicule creerVehicule(Vehicule vehicule);

    Vehicule definirQuotaPourVehicule(Long vehiculeId, double quota);

    Station creerStationAvecAdmin(StationAvecAdminRequest request);

    Demande creerDemandePourEntreprise(DemandeRequest request);

    Ticket validerDemandeEtGenererTicket(Long id);

    Demande rejeterDemande(Long id, String motif);

    List<Demande> getDemandesParStatut(String statut);

    Demandeur creerDemandeur(Demandeur demandeur);

    List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId);

    ResponseEntity<Resource> exporterRapportConsommation();

    List<Station> obtenirToutesLesStations();

    List<Station> obtenirStationsParGestionnaire(UUID gestionnaireId);
}
