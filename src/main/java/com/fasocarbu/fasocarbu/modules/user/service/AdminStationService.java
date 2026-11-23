package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;

import java.util.List;
import java.util.UUID;

public interface AdminStationService {
    AdminStation create(AdminStation adminStation, Long stationId);
    List<AdminStation> getAll();
    AdminStation getById(UUID id);
    AdminStation update(UUID id, AdminStation adminStation);
    void delete(UUID id);
}
