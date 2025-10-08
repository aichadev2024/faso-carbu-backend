package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {

    List<Rapport> findTop5ByEntrepriseIdOrderByDateCreationDesc(Long entrepriseId);
}
