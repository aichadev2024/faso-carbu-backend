package com.fasocarbu.fasocarbu.modules.station.web;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.station.service.StationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Stations", description = "Endpoints de gestion des stations essence")
@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @Operation(summary = "Ajouter une station")
    @PostMapping("/ajouter")
    public Station ajouterStation(@RequestBody Station station) {
        return stationService.enregistrerStation(station);
    }

    @Operation(summary = "Obtenir une station par ID")
    @GetMapping("/{id}")
    public Station getStation(@PathVariable Long id) {
        return stationService.getStationById(id);
    }

    @Operation(summary = "Lister toutes les stations")
    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @Operation(summary = "Supprimer une station")
    @DeleteMapping("/{id}")
    public void supprimerStation(@PathVariable Long id) {
        stationService.supprimerStation(id);
    }
}
