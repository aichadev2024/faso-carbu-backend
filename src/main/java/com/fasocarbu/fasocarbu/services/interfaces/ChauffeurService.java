package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import java.util.List;

public interface ChauffeurService {

    Chauffeur ajouterChauffeur(Chauffeur chauffeur);

    Chauffeur getChauffeur(Long id);

    List<Chauffeur> getTousLesChauffeurs();

    Chauffeur updateChauffeur(Long id, Chauffeur chauffeur);

    void supprimerChauffeur(Long id);
}
