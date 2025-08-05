package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Gestionnaire;
import com.fasocarbu.fasocarbu.repositories.GestionnaireRepository;
import com.fasocarbu.fasocarbu.services.interfaces.GestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GestionnaireServiceImpl implements GestionnaireService {

    @Autowired
    private GestionnaireRepository repository;

    @Override
    public Gestionnaire ajouterGestionnaire(Gestionnaire gestionnaire) {
        return repository.save(gestionnaire);
    }

    @Override
    public Gestionnaire obtenirGestionnaire(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Gestionnaire> obtenirTousLesGestionnaires() {
        return repository.findAll();
    }

    @Override
    public Gestionnaire modifierGestionnaire(UUID id, Gestionnaire gestionnaire) {
        Optional<Gestionnaire> optional = repository.findById(id);
        if (optional.isPresent()) {
            gestionnaire.setId(id);
            return repository.save(gestionnaire);
        }
        return null;
    }

    @Override
    public void supprimerGestionnaire(UUID id) {
        repository.deleteById(id);
    }
}
