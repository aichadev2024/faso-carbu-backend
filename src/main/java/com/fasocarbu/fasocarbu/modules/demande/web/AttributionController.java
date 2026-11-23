package com.fasocarbu.fasocarbu.modules.demande.web;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;
import com.fasocarbu.fasocarbu.modules.demande.service.AttributionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Attributions", description = "Endpoints de gestion des attributions de tickets aux chauffeurs")
@RestController
@RequestMapping("/api/attributions")
public class AttributionController {

    private final AttributionService attributionService;

    public AttributionController(AttributionService attributionService) {
        this.attributionService = attributionService;
    }

    @Operation(summary = "Ajouter une attribution")
    @PostMapping("/ajouter")
    public Attribution ajouterAttribution(@RequestBody Attribution attribution) {
        return attributionService.ajouterAttribution(attribution);
    }

    @Operation(summary = "Lister toutes les attributions")
    @GetMapping
    public List<Attribution> getAll() {
        return attributionService.getAllAttributions();
    }

    @Operation(summary = "Obtenir une attribution par ID")
    @GetMapping("/{id}")
    public Attribution getById(@PathVariable UUID id) {
        return attributionService.getAttributionById(id);
    }

    @Operation(summary = "Supprimer une attribution")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        attributionService.supprimerAttribution(id);
    }
}
