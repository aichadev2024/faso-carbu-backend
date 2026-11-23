package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.user.domain.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, UUID> {
}
