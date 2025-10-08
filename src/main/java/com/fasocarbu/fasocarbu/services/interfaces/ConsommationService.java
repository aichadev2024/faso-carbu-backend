package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Consommation;

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
