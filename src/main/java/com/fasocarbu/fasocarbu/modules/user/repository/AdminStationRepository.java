package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminStationRepository extends JpaRepository<AdminStation, UUID> {
    AdminStation findByEmail(String email);
}
