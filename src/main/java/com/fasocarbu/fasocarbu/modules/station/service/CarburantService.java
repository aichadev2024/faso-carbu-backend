package com.fasocarbu.fasocarbu.modules.station.service;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.dto.CarburantDTO;

import java.util.List;
import java.util.UUID;

public interface CarburantService {
    Carburant ajouterCarburant(Carburant carburant);

    List<Carburant> getAllCarburants();

    Carburant getCarburantById(Long id);

    void supprimerCarburant(Long id);

    Carburant updatePrix(Long idCarburant, UUID idAdminStation, Double nouveauPrix);

    List<CarburantDTO> getAllCarburantsDTO();

    CarburantDTO getCarburantDTOById(Long id);

    CarburantDTO updatePrixDTO(Long idCarburant, UUID idAdminStation, Double nouveauPrix);

    Carburant ajouterCarburantPourStation(UUID adminStationId, Carburant carburant);

    List<Carburant> getCarburantsByAdminStation(UUID adminStationId);

    List<CarburantDTO> getAllCarburantsDTOByEntreprise(Long entrepriseId);

    Carburant updateCarburant(UUID adminStationId, Long carburantId, Carburant carburant);
}
