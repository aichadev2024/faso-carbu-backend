package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.AdminStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminStationRepository extends JpaRepository<AdminStation, UUID> {
    AdminStation findByEmail(String email);
}
