
package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
import com.fasocarbu.fasocarbu.dtos.GestionnaireAvecEntrepriseRequest;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GestionnaireServiceImpl implements GestionnaireService {

    @Autowired
    private GestionnaireRepository gestionnaireRepository;
    @Autowired
    private ChauffeurRepository chauffeurRepository;
    @Autowired
    private VehiculeRepository vehiculeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private AdminStationRepository adminStationRepository;
    @Autowired
    private CarburantRepository carburantRepository;
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private DemandeurRepository demandeurRepository;
    @Autowired
    private ConsommationRepository consommationRepository;

    @Override
    public Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire) {
        return gestionnaireRepository.save(gestionnaire);
    }

    @Override
    public Gestionnaire obtenirGestionnaire(UUID id) {
        return gestionnaireRepository.findById(id).orElse(null);
    }

    @Override
    public List<Gestionnaire> obtenirTousLesGestionnaires() {
        return gestionnaireRepository.findAll();
    }

    @Override
    public Gestionnaire modifierGestionnaire(UUID id, Gestionnaire gestionnaire) {
        return gestionnaireRepository.findById(id)
                .map(existing -> {
                    gestionnaire.setId(id);
                    return gestionnaireRepository.save(gestionnaire);
                })
                .orElse(null);
    }

    @Override
    public void supprimerGestionnaire(UUID id) {
        gestionnaireRepository.deleteById(id);
    }

    @Override
    public Chauffeur creerChauffeur(Chauffeur chauffeur) {
        chauffeur.setRole("CHAUFFEUR");
        return chauffeurRepository.save(chauffeur);
    }

    @Override
    public Vehicule creerVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Station creerStationAvecAdmin(StationAvecAdminRequest request) {
        Station station = new Station();
        station.setNom(request.getNomStation());
        station.setAdresse(request.getAdresseStation());
        station.setVille(request.getVilleStation()); // ⚠️ ajouter la ville
        station.setStatut("ACTIF"); // ou le statut que tu veux par défaut
        Station savedStation = stationRepository.save(station);

        AdminStation admin = new AdminStation();
        admin.setNom(request.getNomAdmin());
        admin.setPrenom(request.getPrenomAdmin());
        admin.setEmail(request.getEmailAdmin());
        admin.setTelephone(request.getTelephoneAdmin());
        admin.setMotDePasse(request.getMotDePasseAdmin());
        admin.setRole("ADMIN_STATION");
        admin.setStation(savedStation);

        adminStationRepository.save(admin);

        // Recharge la station complète avec admin pour retourner tous les champs
        return stationRepository.findById(savedStation.getId()).orElse(savedStation);
    }

    @Override
    public Demande creerDemandePourEntreprise(DemandeRequest request) {
        Demande demande = new Demande();
        demande.setDateDemande(LocalDateTime.now());
        demande.setQuantite(request.getQuantite());
        demande.setCarburant(carburantRepository.findById(request.getCarburantId()).orElse(null));
        demande.setStation(stationRepository.findById(request.getStationId()).orElse(null));
        demande.setVehicule(vehiculeRepository.findById(request.getVehiculeId()).orElse(null));
        demande.setGestionnaire(gestionnaireRepository.findById(request.getGestionnaireId()).orElse(null));
        demande.setStatut(com.fasocarbu.fasocarbu.enums.StatutDemande.EN_ATTENTE);
        return demandeRepository.save(demande);
    }

    @Override
    public Ticket validerDemandeEtGenererTicket(Long id) {
        Optional<Demande> optionalDemande = demandeRepository.findById(id);
        if (optionalDemande.isPresent()) {
            Demande demande = optionalDemande.get();
            demande.setStatut(com.fasocarbu.fasocarbu.enums.StatutDemande.VALIDEE);
            demande.setDateValidation(LocalDateTime.now());

            Ticket ticket = new Ticket();
            ticket.setDemande(demande);
            ticket.setDateEmission(LocalDateTime.now());
            ticket.setCodeQr(UUID.randomUUID().toString());

            demande.setTicket(ticket);

            demandeRepository.save(demande);
            ticketRepository.save(ticket);

            return ticket;
        }
        return null;
    }

    @Override
    public Demande rejeterDemande(Long id, String motif) {
        Optional<Demande> optionalDemande = demandeRepository.findById(id);
        if (optionalDemande.isPresent()) {
            Demande demande = optionalDemande.get();
            demande.setStatut(com.fasocarbu.fasocarbu.enums.StatutDemande.REJETEE);
            demande.setDateValidation(LocalDateTime.now());
            demande.setMotifRejet(motif);
            return demandeRepository.save(demande);
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> exporterRapportConsommation() {
        throw new UnsupportedOperationException("Rapport non encore implémenté.");
    }

    @Override
    public Gestionnaire ajouterGestionnaireAvecEntreprise(GestionnaireAvecEntrepriseRequest request) {
        Entreprise entreprise = entrepriseRepository.findByNom(request.getNomEntreprise())
                .orElseGet(() -> entrepriseRepository.save(new Entreprise(request.getNomEntreprise())));

        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setNom(request.getNom());
        gestionnaire.setPrenom(request.getPrenom());
        gestionnaire.setEmail(request.getEmail());
        gestionnaire.setMotDePasse(request.getMotDePasse());
        gestionnaire.setTelephone(request.getTelephone());
        gestionnaire.setRole("GESTIONNAIRE");
        gestionnaire.setEntreprise(entreprise);

        return gestionnaireRepository.save(gestionnaire);
    }

    @Override
    public List<Demande> getDemandesParStatut(String statut) {
        return demandeRepository.findByStatut(StatutDemande.valueOf(statut.toUpperCase()));
    }

    @Override
    public Demandeur creerDemandeur(Demandeur demandeur) {
        demandeur.setRole("DEMANDEUR");
        return demandeurRepository.save(demandeur);
    }

    @Override
    public List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId) {
        return consommationRepository.findByAttribution_Ticket_Vehicule_Id(vehiculeId);
    }

    @Override
    public Vehicule definirQuotaPourVehicule(Long vehiculeId, double quota) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        vehicule.setQuotaCarburant(quota);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public List<Station> obtenirToutesLesStations() {
        return stationRepository.findAll();
    }

    @Override
    public List<Station> obtenirStationsParGestionnaire(UUID gestionnaireId) {
        return stationRepository.findByGestionnaireId(gestionnaireId);
    }

}
