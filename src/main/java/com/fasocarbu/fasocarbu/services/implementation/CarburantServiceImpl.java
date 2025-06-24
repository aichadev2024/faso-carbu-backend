package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.repositories.CarburantRepository;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarburantServiceImpl implements CarburantService {

    @Autowired
    private CarburantRepository carburantRepository;

    @Override
    public Carburant ajouterCarburant(Carburant carburant) {
        return carburantRepository.save(carburant);
    }

    @Override
    public List<Carburant> getAllCarburants() {
        return carburantRepository.findAll();
    }

    @Override
    public Carburant getCarburantById(Long id) {
        return carburantRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerCarburant(Long id) {
        carburantRepository.deleteById(id);
    }
}
