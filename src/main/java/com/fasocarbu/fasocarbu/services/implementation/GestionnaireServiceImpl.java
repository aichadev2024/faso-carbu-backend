package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.DemandeRequest;
import com.fasocarbu.fasocarbu.dtos.GestionnaireAvecEntrepriseRequest;
import com.fasocarbu.fasocarbu.dtos.StationAvecAdminRequest;
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

    // ------------------- Chauffeurs -------------------
    @Override
    public Chauffeur creerChauffeur(Chauffeur chauffeur) {
        chauffeur.setRole("CHAUFFEUR");
        chauffeur.setMotDePasse(passwordEncoder.encode(chauffeur.getMotDePasse()));
        return chauffeurRepository.save(chauffeur);
    }

    // ------------------- Véhicules -------------------
    @Override
    public Vehicule creerVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    // ------------------- Stations avec Admin -------------------
    @Override
    public Station creerStationAvecAdmin(StationAvecAdminRequest request, UUID gestionnaireId) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé"));

        Station station = new Station();
        station.setNom(request.getNomStation());
        station.setAdresse(request.getAdresseStation());
        station.setVille(request.getVilleStation());
        station.setStatut("ACTIF");
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

        return stationRepository.findById(savedStation.getId()).orElse(savedStation);
    }

    @Override
    public List<Station> obtenirToutesLesStations() {
        return stationRepository.findAll();
    }

    // ------------------- Demandes -------------------
    @Override
    public Demande creerDemandePourEntreprise(DemandeRequest request) {
        Demande demande = new Demande();
        demande.setDateDemande(LocalDateTime.now());
        demande.setQuantite(request.getQuantite());
        demande.setCarburant(carburantRepository.findById(request.getCarburantId()).orElse(null));
        demande.setStation(stationRepository.findById(request.getStationId()).orElse(null));
        demande.setVehicule(vehiculeRepository.findById(request.getVehiculeId()).orElse(null));
        demande.setGestionnaire(gestionnaireRepository.findById(request.getGestionnaireId()).orElse(null));
        demande.setStatut(StatutDemande.EN_ATTENTE);
        return demandeRepository.save(demande);
    }

    @Override
    public Ticket validerDemandeEtGenererTicket(Long id) {
        Optional<Demande> optionalDemande = demandeRepository.findById(id);
        if (optionalDemande.isPresent()) {
            Demande demande = optionalDemande.get();
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

            try {
                // ✅ Générer le QR Code en Base64
                String qrCode = qrCodeGenerator.generateQRCodeForTicket(ticket);
                ticket.setCodeQr(qrCode);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la génération du QR Code", e);
            }

            demande.setTicket(ticket);

            demandeRepository.save(demande);
            ticketRepository.save(ticket);

            return ticket;
        }
        return null;
    }

    @Override
    public Demande rejeterDemande(Long id, String motif) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("Demande déjà traitée");
        }

        demande.setStatut(StatutDemande.REJETEE);
        demande.setDateValidation(LocalDateTime.now());
        demande.setMotifRejet(motif);

        demandeRepository.save(demande);

        notificationService.sendNotificationToUtilisateur(
                demande.getDemandeur().getId().toString(),
                "Votre demande a été rejetée : " + motif);

        return demande;
    }

    @Override
    public List<Demande> getDemandesParStatut(String statut) {
        return demandeRepository.findByStatut(StatutDemande.valueOf(statut.toUpperCase()));
    }

    // ------------------- Gestionnaire avec entreprise -------------------
    @Override
    public Gestionnaire ajouterGestionnaireAvecEntreprise(GestionnaireAvecEntrepriseRequest request) {
        Entreprise entreprise = entrepriseRepository.findByNom(request.getNomEntreprise())
                .orElseGet(() -> entrepriseRepository.save(new Entreprise(request.getNomEntreprise())));

        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setNom(request.getNom());
        gestionnaire.setPrenom(request.getPrenom());
        gestionnaire.setEmail(request.getEmail());
        gestionnaire.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        gestionnaire.setTelephone(request.getTelephone());
        gestionnaire.setRole("GESTIONNAIRE");
        gestionnaire.setEntreprise(entreprise);

        return gestionnaireRepository.save(gestionnaire);
    }

    // ------------------- Demandeurs -------------------
    @Override
    public Demandeur creerDemandeur(Demandeur demandeur) {
        demandeur.setRole("DEMANDEUR");
        demandeur.setMotDePasse(passwordEncoder.encode(demandeur.getMotDePasse()));
        return demandeurRepository.save(demandeur);
    }

    // ------------------- Consommation -------------------
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

    // ------------------- Rapports -------------------
    @Override
    public ResponseEntity<Resource> exporterRapportConsommation() {
        throw new UnsupportedOperationException("Rapport non encore implémenté.");
    }

}
