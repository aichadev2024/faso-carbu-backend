package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Gestionnaire;

import java.util.List;

public interface GestionnaireService {
    Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire);
    Gestionnaire obtenirGestionnaire(Long id);
    List<Gestionnaire> obtenirTousLesGestionnaires();
    Gestionnaire modifierGestionnaire(Long id, Gestionnaire gestionnaire);
    void supprimerGestionnaire(Long id);
}
