package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, UUID> {
}
