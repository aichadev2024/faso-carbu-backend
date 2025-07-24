package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Demande;

import java.util.List;

public interface DemandeService {
    Demande creerDemandeAvecTicket(Long chauffeurId, Long carburantId, Long stationId, Long vehiculeId, double quantite);
    Demande saveDemande(Demande demande);
    List<Demande> getAllDemandes();
    List<Demande> getDemandesParDemandeur(Long chauffeurId);
    

}
