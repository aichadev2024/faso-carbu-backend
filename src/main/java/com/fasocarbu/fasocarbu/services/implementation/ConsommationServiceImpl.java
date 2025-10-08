package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Consommation;
import com.fasocarbu.fasocarbu.repositories.ConsommationRepository;
import com.fasocarbu.fasocarbu.services.interfaces.ConsommationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConsommationServiceImpl implements ConsommationService {

    @Autowired
    private ConsommationRepository consommationRepository;

    @Override
    public Consommation enregistrerConsommation(Consommation consommation) {
        return consommationRepository.save(consommation);
    }

    @Override
    public Consommation getConsommationById(UUID id) {
        Optional<Consommation> optional = consommationRepository.findById(id);
        return optional.orElse(null);
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
