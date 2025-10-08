package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Rapport;

import java.util.List;

public interface RapportService {
    Rapport creerRapport(Rapport rapport);

    List<Rapport> findTop5ByEntrepriseOrderByDateCreationDesc(Long entrepriseId);

    List<Rapport> getRapportsByEntreprise(Long entrepriseId);

    Rapport getRapportById(Long id);

    void supprimerRapport(Long id);
}
