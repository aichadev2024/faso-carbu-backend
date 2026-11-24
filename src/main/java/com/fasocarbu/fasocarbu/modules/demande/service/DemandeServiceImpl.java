package com.fasocarbu.fasocarbu.modules.demande.service;

import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.demande.domain.StatutDemande;
import com.fasocarbu.fasocarbu.modules.demande.repository.DemandeRepository;
import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.station.repository.CarburantRepository;
import com.fasocarbu.fasocarbu.modules.station.repository.StationRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.Gestionnaire;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.GestionnaireRepository;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import com.fasocarbu.fasocarbu.modules.vehicule.repository.VehiculeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final StationRepository stationRepository;
    private final CarburantRepository carburantRepository;
    private final GestionnaireRepository gestionnaireRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository,
                              UtilisateurRepository utilisateurRepository,
                              VehiculeRepository vehiculeRepository,
                              StationRepository stationRepository,
                              CarburantRepository carburantRepository,
                              GestionnaireRepository gestionnaireRepository) {
        this.demandeRepository = demandeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.stationRepository = stationRepository;
        this.carburantRepository = carburantRepository;
        this.gestionnaireRepository = gestionnaireRepository;
    }

    @Override
    public Demande creerDemande(UUID demandeurId, UUID gestionnaireId, Long entrepriseId,
                                Long carburantId, Long stationId, Long vehiculeId,
                                Double quantite, UUID chauffeurId) {

        Utilisateur demandeur = utilisateurRepository.findById(demandeurId)
                .orElseThrow(() -> new RuntimeException("Demandeur introuvable"));
        Gestionnaire gestionnaire = gestionnaireRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire introuvable"));

        Utilisateur chauffeur = null;
        if (chauffeurId != null) {
            chauffeur = utilisateurRepository.findById(chauffeurId)
                    .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));
        }

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station introuvable"));
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule introuvable"));
        Carburant carburant = carburantRepository.findById(carburantId)
                .orElseThrow(() -> new RuntimeException("Carburant introuvable"));

        Demande demande = new Demande();
        demande.setDemandeur(demandeur);
        demande.setGestionnaire(gestionnaire);
        demande.setChauffeur(chauffeur);
        demande.setEntreprise(new Entreprise(entrepriseId));
        demande.setStation(station);
        demande.setVehicule(vehicule);
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
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> getDemandesParEntreprise(Long entrepriseId) {
        return demandeRepository.findByEntreprise_Id(entrepriseId);
    }

    @Override
    public List<Demande> getDemandesParDemandeur(UUID utilisateurId) {
        return demandeRepository.findByDemandeur_Id(utilisateurId);
    }
}
