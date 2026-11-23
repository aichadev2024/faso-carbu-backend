package com.fasocarbu.fasocarbu.modules.demande.repository;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttributionRepository extends JpaRepository<Attribution, UUID> {
}
