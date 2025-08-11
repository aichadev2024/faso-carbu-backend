package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, UUID> {

    // Tu peux ajouter d'autres méthodes de recherche si besoin, par exemple :
    // List<Demandeur> findByEntrepriseId(UUID entrepriseId);

}
