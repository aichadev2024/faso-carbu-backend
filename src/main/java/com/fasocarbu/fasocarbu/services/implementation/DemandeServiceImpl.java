package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private CarburantRepository carburantRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private NotificationService notificationService;


    @Override
    public Demande creerDemandeAvecTicket(UUID chauffeurId, Long carburantId, Long stationId, Long vehiculeId, double quantite) {
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId).orElse(null);
        Carburant carburant = carburantRepository.findById(carburantId).orElse(null);
        Station station = stationRepository.findById(stationId).orElse(null);
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId).orElse(null);

        if (chauffeur == null || carburant == null || station == null || vehicule == null) {
            throw new IllegalArgumentException("Chauffeur, Carburant, Station ou Véhicule invalide.");
        }

        Demande demande = new Demande();
        demande.setDateDemande(LocalDateTime.now());
        demande.setQuantite(quantite);
        demande.setChauffeur(chauffeur);
        demande.setCarburant(carburant);
        demande.setStation(station);
        demande.setVehicule(vehicule);
        demande.setStatut("EN_ATTENTE");

        Demande savedDemande = demandeRepository.save(demande);

        // Envoyer une notification aux gestionnaires
        String titre = "Nouvelle demande de ticket";
        String message = "Le chauffeur " + chauffeur.getNom() + " " + chauffeur.getPrenom() + " a soumis une demande.";

        notificationService.sendNotificationToGestionnaires(titre, message);

        return savedDemande;
    }

    @Override
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> getDemandesParDemandeur(UUID chauffeurId) {
        return demandeRepository.findByChauffeurId(chauffeurId);
    }
}
