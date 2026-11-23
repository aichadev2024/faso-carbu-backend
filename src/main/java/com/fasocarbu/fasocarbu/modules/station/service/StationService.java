package com.fasocarbu.fasocarbu.modules.station.service;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;

import java.util.List;

public interface StationService {
    Station enregistrerStation(Station station);

    Station getStationById(Long id);

    List<Station> getAllStations();

    void supprimerStation(Long id);
}
