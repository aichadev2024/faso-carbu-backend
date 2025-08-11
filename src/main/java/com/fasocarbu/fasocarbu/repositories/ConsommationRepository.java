package com.fasocarbu.fasocarbu.repositories;

import com.fasocarbu.fasocarbu.models.Consommation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConsommationRepository extends JpaRepository<Consommation, Long> {
    List<Consommation> findByVehiculeId(Long vehiculeId);

}
