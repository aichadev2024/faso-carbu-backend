package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Vehicule;
import com.fasocarbu.fasocarbu.repositories.VehiculeRepository;
import com.fasocarbu.fasocarbu.services.interfaces.VehiculeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculeServiceImpl implements VehiculeService {

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Override
    public Vehicule enregistrerVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule getVehiculeById(Long id) {
        Optional<Vehicule> optional = vehiculeRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Vehicule> getAllVehicules() {
        return vehiculeRepository.findAll();
    }

    @Override
    public void supprimerVehicule(Long id) {
        vehiculeRepository.deleteById(id);
    }

    @Override
    public List<Vehicule> getVehiculesParEntreprise(Long entrepriseId) {
        return vehiculeRepository.findByUtilisateur_Entreprise_Id(entrepriseId);
    }
}
