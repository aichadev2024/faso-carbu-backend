package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, UUID> {
    List<Chauffeur> findByEntreprise_Id(Long entrepriseId);
}
