package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final StationRepository stationRepository;
    private final CarburantRepository carburantRepository;
    private final TicketRepository ticketRepository;
    private final NotificationService notificationService; 
@Override
public Demande creerDemandeAvecTicket(UUID demandeurId, Long carburantId, Long stationId, Long vehiculeId, double quantite) {
    Utilisateur demandeur = utilisateurRepository.findById(demandeurId)
            .orElseThrow(() -> new RuntimeException("Demandeur introuvable"));

    Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
            .orElseThrow(() -> new RuntimeException("Véhicule introuvable"));

    Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new RuntimeException("Station introuvable"));

    Carburant carburant = carburantRepository.findById(carburantId)
            .orElseThrow(() -> new RuntimeException("Carburant introuvable"));

    Demande demande = new Demande();
    demande.setDemandeur(demandeur); 
    demande.setVehicule(vehicule);
    demande.setStation(station);
    demande.setCarburant(carburant);
    demande.setQuantite(quantite);
    demande.setStatut(StatutDemande.EN_ATTENTE);
    demande.setDateDemande(LocalDateTime.now());

    return demandeRepository.save(demande);
}

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> getDemandesParDemandeur(UUID demandeur) {
        return demandeRepository.findByDemandeur_Id(demandeur); 
    }

    @Override
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public Demande validerDemande(Long demandeId, UUID gestionnaireId) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("Demande déjà traitée");
        }

        Utilisateur gestionnaire = utilisateurRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé"));

        demande.setStatut(StatutDemande.VALIDEE);
        demande.setDateValidation(LocalDateTime.now()); 

        Ticket ticket = new Ticket();
        ticket.setDemande(demande);
        ticket.setStation(demande.getStation());
        ticket.setCarburant(demande.getCarburant());
        ticket.setQuantite(BigDecimal.valueOf(demande.getQuantite()));
        ticket.setDateEmission(LocalDateTime.now()); 
        ticket.setValidateur(gestionnaire);
        ticket.setUtilisateur(demande.getDemandeur());


        ticketRepository.save(ticket);
        demandeRepository.save(demande);

        notificationService.sendNotificationToUtilisateur(
    demande.getDemandeur().getId().toString(),
    "Demande validée"
);


        return demande;
    }

    // Tu dois aussi implémenter cette méthode si elle est dans l'interface :
    @Override
    public Demande rejeterDemande(Long demandeId) {
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        if (demande.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("Demande déjà traitée");
        }

        demande.setStatut(StatutDemande.REJETEE);
        demandeRepository.save(demande);

        notificationService.sendNotificationToUtilisateur(
                demande.getDemandeur().getId().toString(),
                "Demande rejetée"
        );

        return demande;
    }
}
