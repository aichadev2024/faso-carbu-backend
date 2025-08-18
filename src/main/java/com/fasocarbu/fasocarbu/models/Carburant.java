package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity
public class Carburant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prix;

    @ManyToOne
    @JoinColumn(name = "admin_station_id")
    private AdminStation adminStation;

    @OneToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Carburant() {
    }

    public Carburant(Long id, String nom, Double prix) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
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
