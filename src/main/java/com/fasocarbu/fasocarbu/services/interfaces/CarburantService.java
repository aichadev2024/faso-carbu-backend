package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Carburant;

import java.util.List;

public interface CarburantService {
    Carburant ajouterCarburant(Carburant carburant);
    List<Carburant> getAllCarburants();
    Carburant getCarburantById(Long id);
    void supprimerCarburant(Long id);
}
