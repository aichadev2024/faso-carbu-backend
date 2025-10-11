package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.*;
import com.fasocarbu.fasocarbu.enums.StatutAttribution;
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
import org.springframework.transaction.annotation.Transactional;

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
    private AttributionRepository attributionRepository;

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

        notificationService.sendNotificationToUtilisateur(
                demande.getDemandeur().getId().toString(),
                "Votre demande a été rejetée : " + motif);
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

        // 1️⃣ Vérifier la demande et l’entreprise
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Demande introuvable ou ne correspond pas à votre entreprise."));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new IllegalStateException("Cette demande a déjà été traitée (" + demande.getStatut() + ").");
        }

        // 2️⃣ Vérifications de base
        if (demande.getVehicule() == null)
            throw new IllegalArgumentException("La demande ne contient pas de véhicule.");
        if (demande.getStation() == null)
            throw new IllegalArgumentException("La demande ne contient pas de station.");
        if (demande.getCarburant() == null)
            throw new IllegalArgumentException("La demande ne contient pas de carburant.");

        // 3️⃣ Récupérer le chauffeur
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId)
                .orElseThrow(() -> new IllegalArgumentException("Chauffeur introuvable."));

        if (chauffeur.getEntreprise() == null) {
            throw new IllegalArgumentException("Ce chauffeur n'est pas associé à une entreprise valide.");
        }

        if (!chauffeur.getEntreprise().getId().equals(entrepriseId)) {
            throw new IllegalArgumentException("Ce chauffeur n'appartient pas à votre entreprise.");
        }

        // 4️⃣ Mettre à jour la demande
        demande.setStatut(StatutDemande.VALIDEE);
        demande.setDateValidation(LocalDateTime.now());

        // 5️⃣ Créer et sauvegarder le ticket (⚠️ D’abord sauvegarder le ticket seul)
        Ticket ticket = new Ticket();
        ticket.setDemande(demande);
        ticket.setDateEmission(LocalDateTime.now());
        ticket.setCarburant(demande.getCarburant());
        ticket.setStation(demande.getStation());
        ticket.setVehicule(demande.getVehicule());
        ticket.setQuantite(BigDecimal.valueOf(demande.getQuantite()));
        ticket.setValidateur(demande.getGestionnaire());
        ticket.setEntreprise(demande.getEntreprise());

        Ticket savedTicket = ticketRepository.save(ticket);

        // 6️⃣ Créer l’attribution après (le ticket a maintenant un ID)
        Attribution attribution = new Attribution();
        attribution.setChauffeur(chauffeur);
        attribution.setTicket(savedTicket);
        attribution.setDateAttribution(LocalDateTime.now().toLocalDate());
        attribution.setQuantite(demande.getQuantite());
        attribution.setStatutAttribution(StatutAttribution.EN_COURS); // ✅ cohérent avec ton enum

        Attribution savedAttribution = attributionRepository.save(attribution);

        // 7️⃣ Mettre à jour le ticket avec l’attribution
        savedTicket.setAttribution(savedAttribution);

        // 8️⃣ Générer le QR code
        try {
            savedTicket.setCodeQr(qrCodeGenerator.generateQRCodeForTicket(savedTicket));
            savedTicket = ticketRepository.save(savedTicket);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du QR code du ticket.", e);
        }

        // 9️⃣ Lier le ticket validé à la demande
        demande.setTicket(savedTicket);
        demandeRepository.save(demande);

        // 🔔 Notification au demandeur
        if (demande.getDemandeur() != null) {
            notificationService.sendNotificationToUtilisateur(
                    demande.getDemandeur().getId().toString(),
                    "Votre demande de carburant a été validée. Ticket généré avec succès.");
        }

        return savedTicket;
    }

    @Override
    public Demande rejeterDemandeParEntreprise(Long demandeId, String motif, Long entrepriseId) {
        // Récupérer la demande en vérifiant l'entreprise
        Demande demande = demandeRepository.findByIdAndEntreprise_Id(demandeId, entrepriseId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable ou hors entreprise"));

        // Vérifier que la demande est en attente
        if (demande.getStatut() != StatutDemande.EN_ATTENTE)
            throw new IllegalStateException("Demande déjà traitée");

        // Déterminer le destinataire de la notification : demandeur ou gestionnaire
        Utilisateur destinataire;
        if (demande.getDemandeur() != null) {
            destinataire = demande.getDemandeur();
        } else if (demande.getGestionnaire() != null) {
            destinataire = demande.getGestionnaire();
        } else {
            throw new RuntimeException("Aucun utilisateur associé à la demande pour notification");
        }

        // Mettre à jour le statut et la date de validation
        demande.setStatut(StatutDemande.REJETEE);
        demande.setDateValidation(LocalDateTime.now());
        demande.setMotifRejet(motif);

        // Sauvegarder la demande
        demandeRepository.save(demande);

        // Envoyer notification
        notificationService.sendNotificationToUtilisateur(
                destinataire.getId().toString(),
                "Votre demande a été rejetée : " + motif);

        return demande;
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
