package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.repositories.CarburantRepository;
import com.fasocarbu.fasocarbu.repositories.AdminStationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;
import com.fasocarbu.fasocarbu.dtos.CarburantDTO;
import com.fasocarbu.fasocarbu.dtos.StationDTO;
import com.fasocarbu.fasocarbu.dtos.AdminStationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarburantServiceImpl implements CarburantService {

    @Autowired
    private CarburantRepository carburantRepository;

    @Autowired
    private AdminStationRepository adminStationRepository;

    // -------------------- Méthodes classiques --------------------
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
        return carburantRepository.save(carburant);
    }

    // -------------------- Méthodes DTO --------------------
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

    // -------------------- Méthodes spécifiques AdminStation --------------------
    @Override
    public Carburant ajouterCarburantPourStation(UUID adminStationId, Carburant carburant) {
        AdminStation adminStation = adminStationRepository.findById(adminStationId)
                .orElseThrow(() -> new RuntimeException("AdminStation introuvable"));

        carburant.setStation(adminStation.getStation());
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
        return carburantRepository.save(existing);
    }

    // -------------------- Conversion Carburant -> DTO --------------------
    private CarburantDTO convertToDTO(Carburant carburant) {
        CarburantDTO dto = new CarburantDTO();
        dto.setId(carburant.getId());
        dto.setNom(carburant.getNom());
        dto.setPrix(carburant.getPrix());

        // Station
        StationDTO stationDTO = new StationDTO();
        stationDTO.setIdStation(carburant.getStation().getId());
        stationDTO.setNom(carburant.getStation().getNom());
        dto.setStation(stationDTO);

        // AdminStation
        if (carburant.getStation().getAdminStation() != null) {
            AdminStationDTO adminDTO = new AdminStationDTO();
            adminDTO.setId(carburant.getStation().getAdminStation().getId());
            adminDTO.setNom(carburant.getStation().getAdminStation().getNom());
            dto.setAdminStation(adminDTO);
        }

        return dto;
    }
}
