package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.AgentStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentStationRepository extends JpaRepository<AgentStation, Integer> {
    // Tu peux ajouter des méthodes spécifiques si besoin
}
