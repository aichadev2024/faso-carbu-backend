package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur, UUID> {
    List<Chauffeur> findByEntreprise_Id(Long entrepriseId);
}
