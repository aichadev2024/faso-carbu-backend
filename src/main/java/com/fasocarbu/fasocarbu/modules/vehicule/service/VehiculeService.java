package com.fasocarbu.fasocarbu.modules.vehicule.service;

import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;

import java.util.List;

public interface VehiculeService {
    Vehicule enregistrerVehicule(Vehicule vehicule);

    Vehicule getVehiculeById(Long id);

    List<Vehicule> getAllVehicules();

    void supprimerVehicule(Long id);

    List<Vehicule> getVehiculesParEntreprise(Long entrepriseId);
}
