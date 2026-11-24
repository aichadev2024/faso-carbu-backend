package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import com.fasocarbu.fasocarbu.modules.user.repository.ChauffeurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChauffeurServiceImpl implements ChauffeurService {

    private final ChauffeurRepository chauffeurRepository;

    public ChauffeurServiceImpl(ChauffeurRepository chauffeurRepository) {
        this.chauffeurRepository = chauffeurRepository;
    }

    @Override
    public Chauffeur ajouterChauffeur(Chauffeur chauffeur) {
        return chauffeurRepository.save(chauffeur);
    }

    @Override
    public Chauffeur getChauffeur(UUID id) {
        return chauffeurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Chauffeur> getTousLesChauffeurs() {
        return chauffeurRepository.findAll();
    }

    @Override
    public Chauffeur updateChauffeur(UUID id, Chauffeur chauffeur) {
        Optional<Chauffeur> existing = chauffeurRepository.findById(id);
        if (existing.isPresent()) {
            chauffeur.setId(id);
            return chauffeurRepository.save(chauffeur);
        }
        return null;
    }

    @Override
    public void supprimerChauffeur(UUID id) {
        chauffeurRepository.deleteById(id);
    }
}
