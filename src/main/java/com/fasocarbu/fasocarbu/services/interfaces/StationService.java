package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Station;

import java.util.List;

public interface StationService {
    Station enregistrerStation(Station station);
    Station getStationById(Long id);
    List<Station> getAllStations();
    void supprimerStation(Long id);
}
