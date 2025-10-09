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

    @Override
    public Demande creerDemandeAvecTicket(UUID userId, Long carburantId, Long stationId, Long vehiculeId,
            double quantite) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule introuvable"));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station introuvable"));

        Carburant carburant = carburantRepository.findById(carburantId)
                .orElseThrow(() -> new RuntimeException("Carburant introuvable"));

        Demande demande = new Demande();
        demande.setVehicule(vehicule);
        demande.setStation(station);
        demande.setCarburant(carburant);
        demande.setQuantite(quantite);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateDemande(LocalDateTime.now());

        // ✅ Affectation selon le type d'utilisateur
        if (user instanceof Gestionnaire) {
            demande.setGestionnaire((Gestionnaire) user);
        } else if (user instanceof Demandeur) {
            demande.setDemandeur((Demandeur) user);
        } else {
            throw new RuntimeException("Seuls un gestionnaire ou un demandeur peuvent créer une demande");
        }

        // ✅ Affectation de l'entreprise
        demande.setEntreprise(user.getEntreprise());

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
