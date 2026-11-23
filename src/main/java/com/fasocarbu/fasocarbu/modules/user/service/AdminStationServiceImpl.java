package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.station.repository.StationRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasocarbu.fasocarbu.modules.user.repository.AdminStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminStationServiceImpl implements AdminStationService {

    private final AdminStationRepository adminStationRepository;
    private final StationRepository stationRepository;

    public AdminStationServiceImpl(AdminStationRepository adminStationRepository, StationRepository stationRepository) {
        this.adminStationRepository = adminStationRepository;
        this.stationRepository = stationRepository;
    }

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
