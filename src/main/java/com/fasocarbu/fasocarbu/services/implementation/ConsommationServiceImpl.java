package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Consommation;
import com.fasocarbu.fasocarbu.repositories.ConsommationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.ConsommationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsommationServiceImpl implements ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    @Override
    public Consommation enregistrerConsommation(Consommation consommation) {
        return consommationRepository.save(consommation);
    }

    @Override
    public Consommation getConsommationById(Long id) {
        Optional<Consommation> optional = consommationRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Consommation> getAllConsommations() {
        return consommationRepository.findAll();
    }

    @Override
    public void supprimerConsommation(Long id) {
        consommationRepository.deleteById(id);
    }
}
