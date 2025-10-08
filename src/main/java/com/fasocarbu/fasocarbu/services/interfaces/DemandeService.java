package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Demande;

import java.util.List;
import java.util.UUID;

public interface DemandeService {
    Demande creerDemandeAvecTicket(UUID userId, Long carburantId, Long stationId, Long vehiculeId, double quantite);

    Demande saveDemande(Demande demande);

    List<Demande> getAllDemandes();

    List<Demande> getDemandesParDemandeur(UUID utilisateurId);

    List<Demande> getDemandesParEntreprise(Long entrepriseId);

}
