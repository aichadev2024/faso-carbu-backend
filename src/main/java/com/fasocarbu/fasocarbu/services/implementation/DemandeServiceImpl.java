package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.repositories.DemandeRepository;
import com.fasocarbu.fasocarbu.services.interfaces.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Override
    public Demande creerDemande(Demande demande) {
        if (demande.getDemandeur() == null || demande.getStation() == null || demande.getQuantite() == null) {
            throw new IllegalArgumentException("❌ Données manquantes dans la demande.");
        }

        Demande saved = demandeRepository.save(demande);
        System.out.println("✅ Nouvelle demande enregistrée : " + saved);
        return saved;
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }
}