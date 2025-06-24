package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity

public class Consommation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_consommation;
    private LocalDate date;
    private Double quantite;
    @ManyToOne
    @JoinColumn(name="id_ticket")
    private Ticket ticket;
    @ManyToOne
    @JoinColumn(name="id_vehicule")
    private Vehicule vehicule;
    public Consommation(){}

    public long getId() {
        return id_consommation;
    }

    public void setId(long id) {
        this.id_consommation = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
}
