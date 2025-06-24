package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.Station;
import com.fasocarbu.fasocarbu.services.interfaces.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    private StationService stationService;

    @PostMapping("/ajouter")
    public Station ajouterStation(@RequestBody Station station) {
        return stationService.enregistrerStation(station);
    }

    @GetMapping("/{id}")
    public Station getStation(@PathVariable Long id) {
        return stationService.getStationById(id);
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @DeleteMapping("/{id}")
    public void supprimerStation(@PathVariable Long id) {
        stationService.supprimerStation(id);
    }
}
