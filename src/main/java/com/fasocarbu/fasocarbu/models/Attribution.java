package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Attribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateAttribution;
    private Double quantite;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public Attribution() {}

    public Attribution(LocalDate dateAttribution, Double quantite, Vehicule vehicule, Carburant carburant, Station station) {
        this.dateAttribution = dateAttribution;
        this.quantite = quantite;
        this.vehicule = vehicule;
        this.carburant = carburant;
        this.station = station;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public LocalDate getDateAttribution() { return dateAttribution; }

    public void setDateAttribution(LocalDate dateAttribution) { this.dateAttribution = dateAttribution; }

    public Double getQuantite() { return quantite; }

    public void setQuantite(Double quantite) { this.quantite = quantite; }

    public Vehicule getVehicule() { return vehicule; }

    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Carburant getCarburant() { return carburant; }

    public void setCarburant(Carburant carburant) { this.carburant = carburant; }

    public Station getStation() { return station; }

    public void setStation(Station station) { this.station = station; }
}
