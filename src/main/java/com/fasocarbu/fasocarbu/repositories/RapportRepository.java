package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {

    List<Rapport> findTop5ByEntrepriseIdOrderByDateCreationDesc(Long entrepriseId);

    List<Rapport> findByEntreprise_Id(Long entrepriseId);

    Optional<Rapport> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

}
