package com.fasocarbu.fasocarbu.controllers;

import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.services.interfaces.AdminStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin-stations")
@CrossOrigin(origins = "*")
public class AdminStationController {

    @Autowired
    private AdminStationService adminStationService;

    @PostMapping("/{stationId}")
    public AdminStation createAdminStation(
            @RequestBody AdminStation adminStation,
            @PathVariable Long stationId
    ) {
        return adminStationService.create(adminStation, stationId);
    }

    @GetMapping
    public List<AdminStation> getAllAdminStations() {
        return adminStationService.getAll();
    }

    @GetMapping("/{id}")
    public AdminStation getAdminStationById(@PathVariable UUID id) {
        return adminStationService.getById(id);
    }

    @PutMapping("/{id}")
    public AdminStation updateAdminStation(
            @PathVariable UUID id,
            @RequestBody AdminStation adminStation
    ) {
        return adminStationService.update(id, adminStation);
    }

    @DeleteMapping("/{id}")
    public void deleteAdminStation(@PathVariable UUID id) {
        adminStationService.delete(id);
    }
}
