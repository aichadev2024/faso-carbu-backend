package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDemande;

    private double quantite;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    @JsonIgnoreProperties({"password", "role", "email", "telephone", "dateNaissance", "cin", "username", "authorities", "enabled", "accountNonExpired", "credentialsNonExpired", "accountNonLocked"})
    private Chauffeur chauffeur;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE"; // Ajout du champ statut avec valeur par défaut

    public Demande() {
    }

    public Demande(LocalDate dateDemande, double quantite, Chauffeur chauffeur, Carburant carburant, Station station) {
        this.dateDemande = dateDemande;
        this.quantite = quantite;
        this.chauffeur = chauffeur;
        this.carburant = carburant;
        this.station = station;
        this.statut = "EN_ATTENTE";
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }

    public Carburant getCarburant() {
        return carburant;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
