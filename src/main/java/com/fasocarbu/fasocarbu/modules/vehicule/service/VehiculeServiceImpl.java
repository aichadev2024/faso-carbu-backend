package com.fasocarbu.fasocarbu.modules.vehicule.service;

import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import com.fasocarbu.fasocarbu.modules.vehicule.repository.VehiculeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;

    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    public Vehicule enregistrerVehicule(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule getVehiculeById(Long id) {
        return vehiculeRepository.findById(id).orElse(null);
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
