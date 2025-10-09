package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.Carburant;

public class CarburantDTO {
    private Long id;
    private String nom;
    private double prix;
    private StationDTO station;
    private AdminStationDTO adminStation;

    // Constructeur depuis l'entité
    public CarburantDTO(Carburant carburant) {
        this.id = carburant.getId();
        this.nom = carburant.getNom();
        this.prix = carburant.getPrix();

        if (carburant.getStation() != null) {
            this.station = new StationDTO(carburant.getStation());
        }

        if (carburant.getAdminStation() != null) {
            this.adminStation = new AdminStationDTO(carburant.getAdminStation());
        }
    }

    // getters et setters
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

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public StationDTO getStation() {
        return station;
    }

    public void setStation(StationDTO station) {
        this.station = station;
    }

    public AdminStationDTO getAdminStation() {
        return adminStation;
    }

    public void setAdminStation(AdminStationDTO adminStation) {
        this.adminStation = adminStation;
    }
}
