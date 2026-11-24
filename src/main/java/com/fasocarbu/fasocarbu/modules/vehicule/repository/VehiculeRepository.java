package com.fasocarbu.fasocarbu.modules.vehicule.repository;

import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByImmatriculation(String immatriculation);

    List<Vehicule> findByEntreprise_Id(Long entrepriseId);

    Optional<Vehicule> findByIdAndEntreprise_Id(Long id, Long entrepriseId);

    List<Vehicule> findByUtilisateur_Entreprise_Id(Long entrepriseId);
}
