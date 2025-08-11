package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.AgentStation;
import com.fasocarbu.fasocarbu.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AgentStationRepository extends JpaRepository<AgentStation, UUID> {
    List<AgentStation> findByStation(Station station);
}
