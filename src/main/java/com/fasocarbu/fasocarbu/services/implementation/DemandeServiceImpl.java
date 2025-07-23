package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.repositories.DemandeRepository;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;

    @Autowired
    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> getDemandesParDemandeur(String demandeur) {
        return demandeRepository.findByDemandeur(demandeur);
    }
}
