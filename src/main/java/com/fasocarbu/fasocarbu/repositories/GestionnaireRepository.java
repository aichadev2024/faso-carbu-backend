package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Gestionnaire;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GestionnaireRepository extends JpaRepository<Gestionnaire, UUID> {

}
