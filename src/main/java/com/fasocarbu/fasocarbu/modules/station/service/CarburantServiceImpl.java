package com.fasocarbu.fasocarbu.modules.station.service;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.dto.CarburantDTO;
import com.fasocarbu.fasocarbu.modules.station.repository.CarburantRepository;
import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasocarbu.fasocarbu.modules.user.repository.AdminStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarburantServiceImpl implements CarburantService {

    private final CarburantRepository carburantRepository;
    private final AdminStationRepository adminStationRepository;

    public CarburantServiceImpl(CarburantRepository carburantRepository, AdminStationRepository adminStationRepository) {
        this.carburantRepository = carburantRepository;
        this.adminStationRepository = adminStationRepository;
    }

    @Override
    public Carburant ajouterCarburant(Carburant carburant) {
        return carburantRepository.save(carburant);
    }

    @Override
    public List<Carburant> getAllCarburants() {
        return carburantRepository.findAll();
    }

    @Override
    public Carburant getCarburantById(Long id) {
        return carburantRepository.findById(id).orElse(null);
    }

    @Override
    public void supprimerCarburant(Long id) {
        carburantRepository.deleteById(id);
    }

    @Override
    public Carburant updatePrix(Long idCarburant, UUID idAdminStation, Double nouveauPrix) {
        AdminStation adminStation = adminStationRepository.findById(idAdminStation)
                .orElseThrow(() -> new RuntimeException("AdminStation non trouvé"));

        Carburant carburant = carburantRepository.findById(idCarburant)
                .orElseThrow(() -> new RuntimeException("Carburant non trouvé"));

        if (carburant.getStation().getId() != adminStation.getStation().getId()) {
            throw new RuntimeException("Cet admin n'a pas le droit de modifier ce carburant");
        }

        carburant.setPrix(nouveauPrix);
        carburant.setAdminStation(adminStation);
        return carburantRepository.save(carburant);
    }

    @Override
    public List<CarburantDTO> getAllCarburantsDTO() {
        return carburantRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CarburantDTO getCarburantDTOById(Long id) {
        Carburant carburant = carburantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carburant non trouvé"));
        return convertToDTO(carburant);
    }

    @Override
    public CarburantDTO updatePrixDTO(Long idCarburant, UUID idAdminStation, Double nouveauPrix) {
        Carburant carburant = updatePrix(idCarburant, idAdminStation, nouveauPrix);
        return convertToDTO(carburant);
    }

    @Override
    public Carburant ajouterCarburantPourStation(UUID adminStationId, Carburant carburant) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("AdminStation introuvable"));

        carburant.setStation(adminStation.getStation());
        carburant.setAdminStation(adminStation);

        return carburantRepository.save(carburant);
    }

    @Override
    public List<Carburant> getCarburantsByAdminStation(UUID adminStationId) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("AdminStation introuvable"));

        return carburantRepository.findByStationId(adminStation.getStation().getId());
    }

    @Override
    public Carburant updateCarburant(UUID adminStationId, Long carburantId, Carburant carburant) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("AdminStation introuvable"));

        Carburant existing = carburantRepository.findById(carburantId)
                .orElseThrow(() -> new RuntimeException("Carburant introuvable"));

        if (existing.getStation().getId() != adminStation.getStation().getId()) {
            throw new RuntimeException("Ce carburant n'appartient pas à la station de cet Admin");
        }
        existing.setNom(carburant.getNom());
        existing.setPrix(carburant.getPrix());
        existing.setAdminStation(adminStation);
        return carburantRepository.save(existing);
    }

    @Override
    public List<CarburantDTO> getAllCarburantsDTOByEntreprise(Long entrepriseId) {
        List<Carburant> carburants = carburantRepository.findByAdminStationEntrepriseId(entrepriseId);
        return carburants.stream()
                .map(CarburantDTO::new)
                .collect(Collectors.toList());
    }

    private CarburantDTO convertToDTO(Carburant carburant) {
        return new CarburantDTO(carburant);
    }
}
