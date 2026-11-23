package com.fasocarbu.fasocarbu.modules.demande.service;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;

import java.util.List;
import java.util.UUID;

public interface AttributionService {
    Attribution ajouterAttribution(Attribution attribution);

    List<Attribution> getAllAttributions();

    Attribution getAttributionById(UUID id);

    void supprimerAttribution(UUID id);
}
