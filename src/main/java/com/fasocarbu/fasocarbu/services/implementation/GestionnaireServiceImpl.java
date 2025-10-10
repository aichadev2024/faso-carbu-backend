package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.*;
import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import com.fasocarbu.fasocarbu.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    // ------------------- Gestionnaires -------------------
    @Override
    public Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire) {
        gestionnaire.setMotDePasse(passwordEncoder.encode(gestionnaire.getMotDePasse()));
        gestionnaire.setRole("GESTIONNAIRE");
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
                    if (gestionnaire.getMotDePasse() != null) {
                        gestionnaire.setMotDePasse(passwordEncoder.encode(gestionnaire.getMotDePasse()));
                    }
                    return gestionnaireRepository.save(gestionnaire);
                })
                .orElse(null);
    }

    @Override
    public void supprimerGestionnaire(UUID id) {
        gestionnaireRepository.deleteById(id);
    }

    @Override
    public List<Gestionnaire> obtenirGestionnairesParEntreprise(Long entrepriseId) {
        return gestionnaireRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Chauffeurs -------------------
    @Override
    public Chauffeur creerChauffeur(Chauffeur chauffeur) {
        chauffeur.setRole("CHAUFFEUR");
        chauffeur.setMotDePasse(passwordEncoder.encode(chauffeur.getMotDePasse()));
        return chauffeurRepository.save(chauffeur);
    }

    @Override
    public List<Chauffeur> obtenirChauffeursParEntreprise(Long entrepriseId) {
        return chauffeurRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Véhicules -------------------
    @Override
    public Vehicule creerVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule definirQuotaPourVehicule(Long vehiculeId, double quota) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        vehicule.setQuotaCarburant(quota);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public List<Vehicule> obtenirVehiculesParEntreprise(Long entrepriseId) {
        return vehiculeRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Stations -------------------
    @Override
    public Station creerStationAvecAdmin(StationAvecAdminRequest request, UUID gestionnaireId) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé"));

        Station station = new Station();
        station.setNom(request.getNomStation());
        station.setAdresse(request.getAdresseStation());
        station.setVille(request.getVilleStation());
        station.setStatut("ACTIF");
        station.setEntreprise(gestionnaire.getEntreprise());

        Station savedStation = stationRepository.save(station);

        AdminStation admin = new AdminStation();
        admin.setNom(request.getNomAdmin());
        admin.setPrenom(request.getPrenomAdmin());
        admin.setEmail(request.getEmailAdmin());
        admin.setTelephone(request.getTelephoneAdmin());
        admin.setMotDePasse(passwordEncoder.encode(request.getMotDePasseAdmin()));
        admin.setRole("ADMIN_STATION");
        admin.setStation(savedStation);
        admin.setEntreprise(gestionnaire.getEntreprise());

        adminStationRepository.save(admin);
        return savedStation;
    }

    @Override
    public List<Station> obtenirStationsParEntreprise(Long entrepriseId) {
        return stationRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Demandes -------------------
    @Override
    public List<Demande> obtenirDemandesParEntreprise(Long entrepriseId) {
        return demandeRepository.findByEntreprise_Id(entrepriseId);
    }

    @Override
    public Ticket validerDemandeEtGenererTicketParEntreprise(Long demandeId, Long entrepriseId) {
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId).orElse(null);
        if (demande == null || demande.getStatut() != StatutDemande.EN_ATTENTE)
            return null;

        demande.setStatut(StatutDemande.VALIDEE);
        demande.setDateValidation(LocalDateTime.now());

        Ticket ticket = new Ticket();
        ticket.setDemande(demande);
        ticket.setDateEmission(LocalDateTime.now());
        ticket.setCarburant(demande.getCarburant());
        ticket.setStation(demande.getStation());
        ticket.setVehicule(demande.getVehicule());
        ticket.setQuantite(BigDecimal.valueOf(demande.getQuantite()));
        ticket.setUtilisateur(demande.getDemandeur());
        ticket.setValidateur(demande.getGestionnaire());
        ticket.setEntreprise(demande.getEntreprise());

        try {
            ticket.setCodeQr(qrCodeGenerator.generateQRCodeForTicket(ticket));
        } catch (Exception e) {
            throw new RuntimeException("Erreur génération QR", e);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        demande.setTicket(savedTicket);
        demandeRepository.save(demande);
        return savedTicket;
    }

    @Override
    public Demande rejeterDemandeParEntreprise(Long demandeId, String motif, Long entrepriseId) {
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId).orElse(null);
        if (demande == null || demande.getStatut() != StatutDemande.EN_ATTENTE)
            return null;

        demande.setStatut(StatutDemande.REJETEE);
        demande.setDateValidation(LocalDateTime.now());
        demande.setMotifRejet(motif);
        demandeRepository.save(demande);

        notificationService.sendNotificationToUtilisateur(
                demande.getDemandeur().getId().toString(),
                "Votre demande a été rejetée : " + motif);

        return demande;
    }

    // ------------------- Tickets -------------------
    @Override
    public List<TicketDTO> obtenirTicketsParEntreprise(Long entrepriseId) {
        return ticketRepository.findByEntreprise_Id(entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> getTicketsParChauffeur(UUID chauffeurId, Long entrepriseId) {
        return ticketRepository.findByAttribution_Chauffeur_IdAndEntreprise_Id(chauffeurId, entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> getTicketsParChauffeurEtDates(UUID chauffeurId, LocalDateTime debut, LocalDateTime fin,
            Long entrepriseId) {
        return ticketRepository
                .findByAttribution_Chauffeur_IdAndDateEmissionBetweenAndEntreprise_Id(chauffeurId, debut, fin,
                        entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    // ------------------- Consommation & rapports -------------------
    @Override
    public List<Consommation> consulterHistoriqueConsommationParVehicule(Long vehiculeId) {
        return consommationRepository.findByAttribution_Ticket_Vehicule_Id(vehiculeId);
    }

    @Override
    public ResponseEntity<Resource> exporterRapportConsommation() {
        throw new UnsupportedOperationException("Rapport non encore implémenté.");
    }

    @Override
    public Resource exporterRapportConsommationParEntreprise(Long entrepriseId) {
        // TODO: générer un rapport réel (PDF/Excel) filtré par entreprise
        // Retour temporaire avec la méthode générale
        return exporterRapportConsommation().getBody();
    }

}
