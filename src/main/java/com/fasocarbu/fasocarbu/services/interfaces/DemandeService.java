package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Demande;

import java.util.List;
import java.util.UUID;

public interface DemandeService {
    Demande creerDemande(UUID demandeurId, UUID gestionnaireId, Long entrepriseId,
            Long carburantId, Long stationId, Long vehiculeId, Double quantite, UUID chauffeurId);

    Demande saveDemande(Demande demande);

    List<Demande> getAllDemandes();

    List<Demande> getDemandesParDemandeur(UUID utilisateurId);

    List<Demande> getDemandesParEntreprise(Long entrepriseId);

}
