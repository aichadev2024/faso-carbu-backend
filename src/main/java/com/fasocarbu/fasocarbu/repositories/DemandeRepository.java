package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Demande;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
     List<Demande> findByDemandeur(String demandeur);

}
