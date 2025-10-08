package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Vehicule;

import java.util.List;

public interface VehiculeService {
    Vehicule enregistrerVehicule(Vehicule vehicule);

    Vehicule getVehiculeById(Long id);

    List<Vehicule> getAllVehicules();

    void supprimerVehicule(Long id);

    List<Vehicule> getVehiculesParEntreprise(Long entrepriseId);
}
