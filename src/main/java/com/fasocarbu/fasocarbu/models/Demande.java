package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String demandeur;
    private String station;
    private String dateDemande;
    private Double quantite;

    public Demande() {
    }

    public Demande(String demandeur, String station, String dateDemande, Double quantite) {
        this.demandeur = demandeur;
        this.station = station;
        this.dateDemande = dateDemande;
        this.quantite = quantite;
    }

    public Long getId() {
        return id;
    }

    public String getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(String demandeur) {
        this.demandeur = demandeur;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", demandeur='" + demandeur + '\'' +
                ", station='" + station + '\'' +
                ", dateDemande='" + dateDemande + '\'' +
                ", quantite=" + quantite +
                '}';
    }
}