package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Attribution;
import com.fasocarbu.fasocarbu.repositories.AttributionRepository;
import com.fasocarbu.fasocarbu.services.interfaces.AttributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributionServiceImpl implements AttributionService {

    @Autowired
    private AttributionRepository attributionRepository;

    @Override
    public Attribution ajouterAttribution(Attribution attribution) {
        return attributionRepository.save(attribution);
    }

    @Override
    public List<Attribution> getAllAttributions() {
        return attributionRepository.findAll();
    }

    @Override
    public Attribution getAttributionById(Long id) {
        return attributionRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerAttribution(Long id) {
        attributionRepository.deleteById(id);
    }

}
