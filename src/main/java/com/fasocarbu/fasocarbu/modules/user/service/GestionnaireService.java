package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.demande.dto.DemandeRequest;
import com.fasocarbu.fasocarbu.modules.entreprise.dto.GestionnaireAvecEntrepriseRequest;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import com.fasocarbu.fasocarbu.modules.user.domain.Demandeur;
import com.fasocarbu.fasocarbu.modules.user.domain.Gestionnaire;
import com.fasocarbu.fasocarbu.modules.user.dto.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GestionnaireService {

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

    Ticket validerDemandeEtGenererTicketParEntreprise(Long demandeId, Long entrepriseId, UUID chauffeurId);

    Demande rejeterDemandeParEntreprise(Long demandeId, String motif, Long entrepriseId);

    List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId);

    ResponseEntity<Resource> exporterRapportConsommation();

    Resource exporterRapportConsommationParEntreprise(Long entrepriseId);
}
