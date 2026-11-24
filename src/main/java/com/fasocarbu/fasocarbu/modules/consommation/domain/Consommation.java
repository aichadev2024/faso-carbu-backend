package com.fasocarbu.fasocarbu.modules.consommation.domain;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;
import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Consommation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_entreprise")
    private Entreprise entreprise;

    @ManyToOne
    @JoinColumn(name = "attribution_id", nullable = false)
    private Attribution attribution;

    @ManyToOne
    @JoinColumn(name = "carburant_id", nullable = false)
    private Carburant carburant;

    private LocalDateTime dateConsommation;

    private double quantiteUtilisee;

    private String commentaire;
}
