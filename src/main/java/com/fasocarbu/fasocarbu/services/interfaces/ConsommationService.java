package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import com.fasocarbu.fasocarbu.models.Consommation;

import java.util.List;

public interface ConsommationService {
    Consommation enregistrerConsommation(Consommation consommation);
    Consommation getConsommationById(Long id);
    List<Consommation> getAllConsommations();
    void supprimerConsommation(Long id);

    interface ChauffeurService {
        Chauffeur getChauffeurById(Long id);

        Chauffeur createChauffeur(Chauffeur chauffeur);

        Chauffeur ajouterChauffeur(Chauffeur chauffeur);
        Chauffeur getChauffeur(Long id);
        List<Chauffeur> getTousLesChauffeurs();
        Chauffeur updateChauffeur(Long id, Chauffeur chauffeur);
        void supprimerChauffeur(Long id);
    }
}
