package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
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
    @JsonIgnoreProperties({ "station", "carburants" })
    private AdminStation adminStation;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @JsonIgnoreProperties({ "adminStation" })
    private Station station;

    public Carburant() {
    }

    public Carburant(Long id, String nom, Double prix) {
        this.id = id;
        this.nom = (nom != null && !nom.isEmpty()) ? nom : "Inconnu";
        this.prix = (prix != null) ? prix : 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom != null ? nom : "Inconnu";
    }

    public void setNom(String nom) {
        this.nom = (nom != null && !nom.isEmpty()) ? nom : "Inconnu";
    }

    public Double getPrix() {
        return prix != null ? prix : 0.0;
    }

    public void setPrix(Double prix) {
        this.prix = (prix != null) ? prix : 0.0;
    }

    public AdminStation getAdminStation() {
        return adminStation;
    }

    public void setAdminStation(AdminStation adminStation) {
        this.adminStation = adminStation;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
