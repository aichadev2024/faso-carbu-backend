package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Attribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributionRepository extends JpaRepository<Attribution, UUID> {

}
