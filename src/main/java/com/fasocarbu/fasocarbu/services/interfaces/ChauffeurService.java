package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import java.util.List;
import java.util.UUID;

public interface ChauffeurService {

    Chauffeur ajouterChauffeur(Chauffeur chauffeur);

    Chauffeur getChauffeur(UUID id);

    List<Chauffeur> getTousLesChauffeurs();

    Chauffeur updateChauffeur(UUID id, Chauffeur chauffeur);

    void supprimerChauffeur(UUID id);
}
