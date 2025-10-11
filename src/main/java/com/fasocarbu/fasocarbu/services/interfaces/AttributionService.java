package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Attribution;

import java.util.List;
import java.util.UUID;

public interface AttributionService {
    Attribution ajouterAttribution(Attribution attribution);

    List<Attribution> getAllAttributions();

    Attribution getAttributionById(UUID id);

    void supprimerAttribution(UUID id);

}
