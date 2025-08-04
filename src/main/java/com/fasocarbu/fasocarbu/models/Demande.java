package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Format ISO avec date + heure (ajuste si besoin)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateDemande;

    private double quantite;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    @JsonIgnoreProperties({
        "password", "role", "email", "telephone", "dateNaissance",
        "cin", "username", "authorities", "enabled", "accountNonExpired",
        "credentialsNonExpired", "accountNonLocked"
    })
    private Chauffeur chauffeur;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE";

    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    private Ticket ticket;

    public Demande() {}

    public Demande(LocalDateTime dateDemande, double quantite, Chauffeur chauffeur,
                   Carburant carburant, Station station, Vehicule vehicule) {
        this.dateDemande = dateDemande;
        this.quantite = quantite;
        this.chauffeur = chauffeur;
        this.carburant = carburant;
        this.station = station;
        this.vehicule = vehicule;
        this.statut = "EN_ATTENTE";
    }

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }

    public Carburant getCarburant() { return carburant; }
    public void setCarburant(Carburant carburant) { this.carburant = carburant; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
}
