package com.fasocarbu.fasocarbu.modules.consommation.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ConsommationService {

    Consommation enregistrerConsommation(Consommation consommation);

    Consommation getConsommationById(UUID id);

    List<Consommation> getAllConsommations();

    void supprimerConsommation(UUID id);

    List<Consommation> getConsommationsParEntreprise(Long entrepriseId);

    List<Consommation> filtrerConsommations(Long entrepriseId, Long vehiculeId, LocalDate dateDebut, LocalDate dateFin);
}
