package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Demande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
}
