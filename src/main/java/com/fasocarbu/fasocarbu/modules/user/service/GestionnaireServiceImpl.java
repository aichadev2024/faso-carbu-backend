package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.core.util.QRCodeGenerator;
import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.consommation.repository.ConsommationRepository;
import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.demande.domain.StatutDemande;
import com.fasocarbu.fasocarbu.modules.demande.dto.DemandeRequest;
import com.fasocarbu.fasocarbu.modules.demande.repository.DemandeRepository;
import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.entreprise.dto.GestionnaireAvecEntrepriseRequest;
import com.fasocarbu.fasocarbu.modules.entreprise.repository.EntrepriseRepository;
import com.fasocarbu.fasocarbu.modules.notification.service.NotificationService;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.station.repository.CarburantRepository;
import com.fasocarbu.fasocarbu.modules.station.repository.StationRepository;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.ticket.dto.TicketDTO;
import com.fasocarbu.fasocarbu.modules.ticket.repository.TicketRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.*;
import com.fasocarbu.fasocarbu.modules.user.dto.StationAvecAdminRequest;
import com.fasocarbu.fasocarbu.modules.user.repository.*;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import com.fasocarbu.fasocarbu.modules.vehicule.repository.VehiculeRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GestionnaireServiceImpl implements GestionnaireService {

    private final GestionnaireRepository gestionnaireRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final StationRepository stationRepository;
    private final AdminStationRepository adminStationRepository;
    private final CarburantRepository carburantRepository;
    private final DemandeRepository demandeRepository;
    private final TicketRepository ticketRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final DemandeurRepository demandeurRepository;
    private final ConsommationRepository consommationRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final QRCodeGenerator qrCodeGenerator;

    public GestionnaireServiceImpl(GestionnaireRepository gestionnaireRepository,
                                   ChauffeurRepository chauffeurRepository,
                                   VehiculeRepository vehiculeRepository,
                                   StationRepository stationRepository,
                                   AdminStationRepository adminStationRepository,
                                   CarburantRepository carburantRepository,
                                   DemandeRepository demandeRepository,
                                   TicketRepository ticketRepository,
                                   EntrepriseRepository entrepriseRepository,
                                   DemandeurRepository demandeurRepository,
                                   ConsommationRepository consommationRepository,
                                   PasswordEncoder passwordEncoder,
                                   NotificationService notificationService,
                                   QRCodeGenerator qrCodeGenerator) {
        this.gestionnaireRepository = gestionnaireRepository;
        this.chauffeurRepository = chauffeurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.stationRepository = stationRepository;
        this.adminStationRepository = adminStationRepository;
        this.carburantRepository = carburantRepository;
        this.demandeRepository = demandeRepository;
        this.ticketRepository = ticketRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.demandeurRepository = demandeurRepository;
        this.consommationRepository = consommationRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

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

    @Override
    public Long getEntrepriseIdFromUser(UUID userId) {
        Utilisateur utilisateur = gestionnaireRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + userId));

        if (utilisateur.getEntreprise() == null) {
            throw new RuntimeException("L'utilisateur n'est associé à aucune entreprise");
        }
        return utilisateur.getEntreprise().getId();
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
        admin.setNomUtilisateur(request.getNomUtilisateurAdmin());
        admin.setMotDePasse(passwordEncoder.encode(request.getMotDePasseAdmin()));
        admin.setRole("ADMIN_STATION");
        admin.setStation(savedStation);
        admin.setEntreprise(gestionnaire.getEntreprise());

        adminStationRepository.save(admin);
        return savedStation;
    }

    @Override
    public List<Station> obtenirToutesLesStations() {
        return stationRepository.findAll();
    }

    @Override
    public List<Station> obtenirStationsParEntreprise(Long entrepriseId) {
        return stationRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Gestionnaires par entreprise -------------------
    @Override
    public List<Gestionnaire> obtenirGestionnairesParEntreprise(Long entrepriseId) {
        return gestionnaireRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Demandeurs -------------------
    @Override
    public Demandeur creerDemandeur(Demandeur demandeur) {
        demandeur.setRole("DEMANDEUR");
        demandeur.setMotDePasse(passwordEncoder.encode(demandeur.getMotDePasse()));
        return demandeurRepository.save(demandeur);
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

        if (request.getDemandeurId() != null) {
            demande.setDemandeur(demandeurRepository.findById(request.getDemandeurId())
                    .orElseThrow(() -> new RuntimeException("Demandeur introuvable")));
        }

        if (request.getChauffeurId() != null) {
            demande.setChauffeur(chauffeurRepository.findById(request.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur introuvable")));
        }

        return demandeRepository.save(demande);
    }

    @Override
    public Ticket validerDemandeEtGenererTicket(Long id) {
        Optional<Demande> optionalDemande = demandeRepository.findById(id);
        if (optionalDemande.isEmpty())
            return null;

        Demande demande = optionalDemande.get();

        if (demande.getStatut() == StatutDemande.VALIDEE && demande.getTicket() != null)
            throw new IllegalStateException("Demande déjà validée");

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
    public Demande rejeterDemande(Long id, String motif) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE)
            throw new RuntimeException("Demande déjà traitée");

        demande.setStatut(StatutDemande.REJETEE);
        demande.setDateValidation(LocalDateTime.now());
        demande.setMotifRejet(motif);
        demandeRepository.save(demande);

        if (demande.getDemandeur() != null) {
            notificationService.sendNotificationToUtilisateur(
                    demande.getDemandeur().getId().toString(),
                    "Votre demande a été rejetée : " + motif);
        }
        return demande;
    }

    @Override
    public List<Demande> getDemandesParStatut(String statut) {
        return demandeRepository.findByStatut(StatutDemande.valueOf(statut.toUpperCase()));
    }

    @Override
    public List<Demande> obtenirDemandesParEntreprise(Long entrepriseId) {
        return demandeRepository.findByEntreprise_Id(entrepriseId);
    }

    // ------------------- Tickets -------------------
    @Override
    public List<TicketDTO> getTicketsParChauffeur(UUID chauffeurId) {
        return ticketRepository.findByAttribution_Chauffeur_Id(chauffeurId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> getTicketsParChauffeurEtDates(UUID chauffeurId, LocalDateTime debut, LocalDateTime fin) {
        return ticketRepository.findByAttribution_Chauffeur_IdAndDateEmissionBetween(chauffeurId, debut, fin)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> obtenirTicketsParEntreprise(Long entrepriseId) {
        return ticketRepository.findByEntreprise_Id(entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> getTicketsParChauffeurEtEntreprise(UUID chauffeurId, Long entrepriseId) {
        return ticketRepository.findByAttribution_Chauffeur_IdAndEntreprise_Id(chauffeurId, entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    public List<TicketDTO> getTicketsParChauffeurEtDatesEtEntreprise(UUID chauffeurId, LocalDateTime debut,
                                                                      LocalDateTime fin, Long entrepriseId) {
        return ticketRepository.findByAttribution_Chauffeur_IdAndDateEmissionBetweenAndEntreprise_Id(
                chauffeurId, debut, fin, entrepriseId)
                .stream().map(TicketDTO::new).toList();
    }

    @Override
    @Transactional
    public Ticket validerDemandeEtGenererTicketParEntreprise(Long demandeId, Long entrepriseId, UUID chauffeurId) {
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Demande introuvable ou ne correspond pas à votre entreprise."));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée (" + demande.getStatut() + ").");
        }

        if (demande.getVehicule() == null)
            throw new IllegalArgumentException("La demande ne contient pas de véhicule.");
        if (demande.getStation() == null)
            throw new IllegalArgumentException("La demande ne contient pas de station.");
        if (demande.getCarburant() == null)
            throw new IllegalArgumentException("La demande ne contient pas de carburant.");

        Chauffeur chauffeur = null;
        if (chauffeurId != null) {
            chauffeur = chauffeurRepository.findById(chauffeurId)
                    .orElseThrow(() -> new IllegalArgumentException("Chauffeur introuvable."));
            if (!chauffeur.getEntreprise().getId().equals(entrepriseId)) {
                throw new IllegalArgumentException("Ce chauffeur n'appartient pas à votre entreprise.");
            }
        }

        demande.setStatut(StatutDemande.VALIDEE);
        demande.setDateValidation(LocalDateTime.now());

        Ticket ticket = new Ticket();
        ticket.setDemande(demande);
        ticket.setDateEmission(LocalDateTime.now());
        ticket.setCarburant(demande.getCarburant());
        ticket.setStation(demande.getStation());
        ticket.setVehicule(demande.getVehicule());
        ticket.setQuantite(BigDecimal.valueOf(demande.getQuantite()));
        ticket.setValidateur(demande.getGestionnaire());
        ticket.setEntreprise(demande.getEntreprise());

        if (chauffeur != null) {
            ticket.setUtilisateur(chauffeur);
        }

        try {
            ticket.setCodeQr(qrCodeGenerator.generateQRCodeForTicket(ticket));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du QR code.", e);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        demande.setTicket(savedTicket);
        demandeRepository.save(demande);

        if (demande.getDemandeur() != null) {
            notificationService.sendNotificationToUtilisateur(
                    demande.getDemandeur().getId().toString(),
                    "Votre demande de carburant a été validée. Ticket généré avec succès.");
        }

        return savedTicket;
    }

    @Override
    public Demande rejeterDemandeParEntreprise(Long demandeId, String motif, Long entrepriseId) {
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable ou hors entreprise"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE)
            throw new IllegalStateException("Demande déjà traitée");

        Utilisateur destinataire;
        if (demande.getDemandeur() != null) {
            destinataire = demande.getDemandeur();
        } else if (demande.getGestionnaire() != null) {
            destinataire = demande.getGestionnaire();
        } else {
            throw new RuntimeException("Aucun utilisateur associé à la demande pour notification");
        }

        demande.setStatut(StatutDemande.REJETEE);
        demande.setDateValidation(LocalDateTime.now());
        demande.setMotifRejet(motif);

        demandeRepository.save(demande);

        notificationService.sendNotificationToUtilisateur(
                destinataire.getId().toString(),
                "Votre demande a été rejetée : " + motif);

        return demande;
    }

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
        return exporterRapportConsommation().getBody();
    }
}
