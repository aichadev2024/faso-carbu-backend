package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.models.Station;
import com.fasocarbu.fasocarbu.repositories.AdminStationRepository;
import com.fasocarbu.fasocarbu.repositories.StationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.AdminStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminStationServiceImpl implements AdminStationService {

    @Autowired
    private AdminStationRepository adminStationRepository;

    @Autowired
    private StationRepository stationRepository;

    @Override
    public AdminStation create(AdminStation adminStation, Long stationId) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station non trouvée"));
        adminStation.setStation(station);
        return adminStationRepository.save(adminStation);
    }

    @Override
    public List<AdminStation> getAll() {
        return adminStationRepository.findAll();
    }

    @Override
    public AdminStation getById(UUID id) {
        return adminStationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin Station non trouvé"));
    }

    @Override
    public AdminStation update(UUID id, AdminStation updated) {
        AdminStation existing = getById(id);
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setEmail(updated.getEmail());
        existing.setTelephone(updated.getTelephone());
        existing.setActif(updated.getActif());
        return adminStationRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        adminStationRepository.deleteById(id);
    }
}
