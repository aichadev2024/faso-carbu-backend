package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Rapport;
import com.fasocarbu.fasocarbu.repositories.RapportRepository;
import com.fasocarbu.fasocarbu.services.interfaces.RapportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RapportServiceImpl implements RapportService {

    @Autowired
    private RapportRepository rapportRepository;

    @Override
    public Rapport creerRapport(Rapport rapport) {
        return rapportRepository.save(rapport);
    }

    @Override
    public List<Rapport> findTop5ByEntrepriseOrderByDateCreationDesc(Long entrepriseId) {
        return rapportRepository.findTop5ByEntrepriseIdOrderByDateCreationDesc(entrepriseId);
    }

    @Override
    public List<Rapport> getRapportsByEntreprise(Long entrepriseId) {
        return rapportRepository.findAll().stream()
                .filter(r -> r.getEntreprise().getId().equals(entrepriseId))
                .toList();
    }

    @Override
    public Rapport getRapportById(Long id) {
        return rapportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rapport non trouvé avec l'id: " + id));
    }

    @Override
    public void supprimerRapport(Long id) {
        rapportRepository.deleteById(id);
    }
}
