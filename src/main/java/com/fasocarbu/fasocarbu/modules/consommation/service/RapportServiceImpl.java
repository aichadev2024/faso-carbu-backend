package com.fasocarbu.fasocarbu.modules.consommation.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Rapport;
import com.fasocarbu.fasocarbu.modules.consommation.repository.RapportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RapportServiceImpl implements RapportService {

    private final RapportRepository rapportRepository;

    public RapportServiceImpl(RapportRepository rapportRepository) {
        this.rapportRepository = rapportRepository;
    }

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
        return rapportRepository.findByEntreprise_Id(entrepriseId);
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
