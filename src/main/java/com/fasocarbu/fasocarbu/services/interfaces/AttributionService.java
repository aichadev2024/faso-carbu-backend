package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Attribution;

import java.util.List;

public interface AttributionService {
    Attribution ajouterAttribution(Attribution attribution);
    List<Attribution> getAllAttributions();
    Attribution getAttributionById(Long id);
    void supprimerAttribution(Long id);
}
