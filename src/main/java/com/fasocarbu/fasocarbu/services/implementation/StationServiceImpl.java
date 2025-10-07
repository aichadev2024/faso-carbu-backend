package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Station;
import com.fasocarbu.fasocarbu.repositories.StationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository stationRepository;

    @Override
    public Station enregistrerStation(Station station) {
        return stationRepository.save(station);
    }

    @Override
    public Station getStationById(Long id) {
        Optional<Station> optional = stationRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    @Override
    public void supprimerStation(Long id) {
        stationRepository.deleteById(id);
    }

}
