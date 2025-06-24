package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
}
