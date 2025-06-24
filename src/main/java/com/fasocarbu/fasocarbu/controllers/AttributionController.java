package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Attribution;
import com.fasocarbu.fasocarbu.services.interfaces.AttributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributions")
public class AttributionController {

    @Autowired
    private AttributionService attributionService;

    @PostMapping("/ajouter")
    public Attribution ajouterAttribution(@RequestBody Attribution attribution) {
        return attributionService.ajouterAttribution(attribution);
    }

    @GetMapping
    public List<Attribution> getAll() {
        return attributionService.getAllAttributions();
    }

    @GetMapping("/{id}")
    public Attribution getById(@PathVariable Long id) {
        return attributionService.getAttributionById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        attributionService.supprimerAttribution(id);
    }
}
