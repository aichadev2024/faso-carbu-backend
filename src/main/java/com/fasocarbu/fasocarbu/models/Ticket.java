package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.StatutTicket;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_ticket;

    private LocalDate dateEmission;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    private BigDecimal montant;
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    private StatutTicket statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_vehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_station")
    private Station station;

    @Column(columnDefinition = "TEXT")
    private String codeQr;

    // Relation vers la demande correspondante
    @OneToOne
    @JoinColumn(name = "demande_id", unique = true)
    private Demande demande;

    public Ticket() {}

    public long getId() {
        return id_ticket;
    }

    public void setId(long id) {
        this.id_ticket = id;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Carburant getCarburant() {
        return carburant;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public StatutTicket getStatut() {
        return statut;
    }

    public void setStatut(StatutTicket statut) {
        this.statut = statut;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getCodeQr() {
        return codeQr;
    }

    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
