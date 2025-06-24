package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import com.fasocarbu.fasocarbu.repositories.ChauffeurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.ChauffeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChauffeurServiceImpl implements ChauffeurService {

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Override
    public Chauffeur ajouterChauffeur(Chauffeur chauffeur) {
        return chauffeurRepository.save(chauffeur);
    }

    @Override
    public Chauffeur getChauffeur(Long id) {
        return chauffeurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Chauffeur> getTousLesChauffeurs() {
        return chauffeurRepository.findAll();
    }

    @Override
    public Chauffeur updateChauffeur(Long id, Chauffeur chauffeur) {
        Optional<Chauffeur> existing = chauffeurRepository.findById(id);
        if (existing.isPresent()) {
            chauffeur.setId(id); // Long et non int
            return chauffeurRepository.save(chauffeur);
        }
        return null;
    }

    @Override
    public void supprimerChauffeur(Long id) {
        chauffeurRepository.deleteById(id);
    }



}
