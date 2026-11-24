package com.fasocarbu.fasocarbu.modules.demande.service;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;
import com.fasocarbu.fasocarbu.modules.demande.repository.AttributionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AttributionServiceImpl implements AttributionService {

    private final AttributionRepository attributionRepository;

    public AttributionServiceImpl(AttributionRepository attributionRepository) {
        this.attributionRepository = attributionRepository;
    }

    @Override
    public Attribution ajouterAttribution(Attribution attribution) {
        return attributionRepository.save(attribution);
    }

    @Override
    public List<Attribution> getAllAttributions() {
        return attributionRepository.findAll();
    }

    @Override
    public Attribution getAttributionById(UUID id) {
        return attributionRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerAttribution(UUID id) {
        attributionRepository.deleteById(id);
    }
}
