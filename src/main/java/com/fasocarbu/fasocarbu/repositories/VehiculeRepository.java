package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByImmatriculation(String immatriculation);

    List<Vehicule> findByEntreprise_Id(Long entrepriseId);

    Optional<Vehicule> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

    List<Vehicule> findByUtilisateur_Entreprise_Id(Long entrepriseId);

}
