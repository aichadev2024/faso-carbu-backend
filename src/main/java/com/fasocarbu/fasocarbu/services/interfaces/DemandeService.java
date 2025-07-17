package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Demande;
import java.util.List;

public interface DemandeService {
    Demande creerDemande(Demande demande);
    List<Demande> getAllDemandes();
}
