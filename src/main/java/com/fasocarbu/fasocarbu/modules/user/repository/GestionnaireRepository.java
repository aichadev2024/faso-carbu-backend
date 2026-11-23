package com.fasocarbu.fasocarbu.modules.user.repository;

import com.fasocarbu.fasocarbu.modules.user.domain.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GestionnaireRepository extends JpaRepository<Gestionnaire, UUID> {
    List<Gestionnaire> findByEntreprise_Id(Long entrepriseId);
}
