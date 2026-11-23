package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.user.domain.AgentStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgentStationRepository extends JpaRepository<AgentStation, UUID> {
    List<AgentStation> findByStation(Station station);
}
