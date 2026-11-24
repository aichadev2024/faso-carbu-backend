package com.fasocarbu.fasocarbu.modules.station.domain;

import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Carburant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom = "Inconnu";

    @Column(nullable = false)
    private Double prix = 0.0;

    @ManyToOne
    @JoinColumn(name = "admin_station_id")
    @JsonIgnoreProperties({"station", "carburants"})
    private AdminStation adminStation;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @JsonIgnoreProperties({"adminStation"})
    private Station station;

    public Carburant(Long id, String nom, Double prix) {
        this.id = id;
        this.nom = (nom != null && !nom.isEmpty()) ? nom : "Inconnu";
        this.prix = (prix != null) ? prix : 0.0;
    }
}
