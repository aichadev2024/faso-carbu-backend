package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Gestionnaire;

import java.util.List;
import java.util.UUID;
public interface GestionnaireService {
    Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire);
    Gestionnaire obtenirGestionnaire(UUID id);
    List<Gestionnaire> obtenirTousLesGestionnaires();
    Gestionnaire modifierGestionnaire(UUID id, Gestionnaire gestionnaire);
    void supprimerGestionnaire(UUID id);
}
