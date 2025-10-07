package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import com.fasocarbu.fasocarbu.models.Consommation;

import java.util.List;
import java.util.UUID;

public interface ConsommationService {
    Consommation enregistrerConsommation(Consommation consommation);

    Consommation getConsommationById(UUID id);

    List<Consommation> getAllConsommations();

    void supprimerConsommation(UUID id);

    interface ChauffeurService {
        Chauffeur getChauffeurById(UUID id);

        Chauffeur createChauffeur(Chauffeur chauffeur);

        Chauffeur ajouterChauffeur(Chauffeur chauffeur);

        Chauffeur getChauffeur(UUID id);

        List<Chauffeur> getTousLesChauffeurs();

        Chauffeur updateChauffeur(UUID id, Chauffeur chauffeur);

        void supprimerChauffeur(UUID id);

    }

}
