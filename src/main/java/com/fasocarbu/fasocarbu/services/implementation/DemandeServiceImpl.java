package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.*;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final GestionnaireRepository gestionnaireRepository;

    @Override
    public Demande creerDemande(UUID demandeurId, UUID gestionnaireId, Long entrepriseId,
            Long carburantId, Long stationId, Long vehiculeId,
            Double quantite, UUID chauffeurId) { // ✅ ajout du chauffeurId

        Utilisateur demandeur = utilisateurRepository.findById(demandeurId)
                .orElseThrow(() -> new RuntimeException("Demandeur introuvable"));
        Gestionnaire gestionnaire = gestionnaireRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire introuvable"));

        // ✅ Nouveau : récupération du chauffeur
        Utilisateur chauffeur = utilisateurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("Chauffeur introuvable"));

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
