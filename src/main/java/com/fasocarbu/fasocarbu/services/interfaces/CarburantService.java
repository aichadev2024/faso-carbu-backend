package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Carburant;

import java.util.List;
import java.util.UUID;

public interface CarburantService {
    Carburant ajouterCarburant(Carburant carburant);

    List<Carburant> getAllCarburants();

    Carburant getCarburantById(Long id);

    void supprimerCarburant(Long id);

    // Ajout du paramètre idAdminStation
    Carburant updatePrix(Long idCarburant, UUID idAdminStation, Double nouveauPrix);
}
