package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public Demande creerDemandeAvecTicket(Long chauffeurId, Long carburantId, Long stationId, Long vehiculeId, double quantite) {
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId).orElse(null);
        Carburant carburant = carburantRepository.findById(carburantId).orElse(null);
        Station station = stationRepository.findById(stationId).orElse(null);
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId).orElse(null);

        if (chauffeur == null || carburant == null || station == null || vehicule == null) {
            throw new IllegalArgumentException("Chauffeur, Carburant, Station ou Véhicule invalide.");
        }

        Demande demande = new Demande();
        demande.setDateDemande(LocalDate.now());
        demande.setQuantite(quantite);
        demande.setChauffeur(chauffeur);
        demande.setCarburant(carburant);
        demande.setStation(station);
        demande.setVehicule(vehicule);
        demande.setStatut("EN_ATTENTE");

        return demandeRepository.save(demande);
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
    public List<Demande> getDemandesParDemandeur(Long chauffeurId) {
        return demandeRepository.findByChauffeurId(chauffeurId);
    }
}
