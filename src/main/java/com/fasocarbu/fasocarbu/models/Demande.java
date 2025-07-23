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

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Utilisateur chauffeur; // ✅ ajout du chauffeur

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

    public Carburant getCarburant() {
        return carburant;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
    }

    public Utilisateur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Utilisateur chauffeur) {
        this.chauffeur = chauffeur;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", demandeur='" + demandeur + '\'' +
                ", station='" + station + '\'' +
                ", dateDemande='" + dateDemande + '\'' +
                ", quantite=" + quantite +
                ", carburant=" + (carburant != null ? carburant.getNom() : "null") +
                ", chauffeur=" + (chauffeur != null ? chauffeur.getEmail() : "null") +
                '}';
    }
}
