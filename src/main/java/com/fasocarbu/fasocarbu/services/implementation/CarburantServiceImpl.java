package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Carburant;
import com.fasocarbu.fasocarbu.models.AdminStation;
import com.fasocarbu.fasocarbu.repositories.CarburantRepository;
import com.fasocarbu.fasocarbu.repositories.AdminStationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.CarburantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CarburantServiceImpl implements CarburantService {

    @Autowired
    private CarburantRepository carburantRepository;

    @Autowired
    private AdminStationRepository adminStationRepository;

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
}
