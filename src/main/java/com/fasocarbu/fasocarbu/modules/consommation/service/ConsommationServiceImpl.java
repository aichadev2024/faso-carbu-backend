package com.fasocarbu.fasocarbu.modules.consommation.service;

import com.fasocarbu.fasocarbu.modules.consommation.domain.Consommation;
import com.fasocarbu.fasocarbu.modules.consommation.repository.ConsommationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class ConsommationServiceImpl implements ConsommationService {

    private final ConsommationRepository consommationRepository;

    public ConsommationServiceImpl(ConsommationRepository consommationRepository) {
        this.consommationRepository = consommationRepository;
    }

    @Override
    public Consommation enregistrerConsommation(Consommation consommation) {
        return consommationRepository.save(consommation);
    }

    @Override
    public Consommation getConsommationById(UUID id) {
        return consommationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Consommation> getAllConsommations() {
        return consommationRepository.findAll();
    }

    @Override
    public void supprimerConsommation(UUID id) {
        consommationRepository.deleteById(id);
    }

    @Override
    public List<Consommation> getConsommationsParEntreprise(Long entrepriseId) {
        return consommationRepository.findByEntreprise_Id(entrepriseId);
    }

    @Override
    public List<Consommation> filtrerConsommations(Long entrepriseId, Long vehiculeId, LocalDate dateDebut,
                                                   LocalDate dateFin) {
        LocalDateTime debut = (dateDebut != null) ? dateDebut.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime fin = (dateFin != null) ? dateFin.atTime(LocalTime.MAX) : LocalDateTime.MAX;

        if (vehiculeId != null) {
            return consommationRepository
                    .findByEntreprise_IdAndAttribution_Ticket_Vehicule_IdAndDateConsommationBetween(
                            entrepriseId, vehiculeId, debut, fin);
        } else {
            return consommationRepository.findByEntreprise_IdAndDateConsommationBetween(
                    entrepriseId, debut, fin);
        }
    }
}
