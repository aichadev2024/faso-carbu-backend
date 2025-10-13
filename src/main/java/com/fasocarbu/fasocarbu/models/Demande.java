package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.StatutDemande;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime dateDemande;

    private double quantite;

    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    @JsonIgnoreProperties({
            "password", "role", "email", "telephone", "dateNaissance",
            "cin", "username", "authorities", "enabled", "accountNonExpired",
            "credentialsNonExpired", "accountNonLocked"
    })
    private Utilisateur demandeur;
    @ManyToOne
    private Utilisateur chauffeur;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    private StatutDemande statut;
    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
    @OneToOne(mappedBy = "demande", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("demande")
    private Ticket ticket;

    private String motifRejet;

    @ManyToOne
    @JoinColumn(name = "gestionnaire_id")
    private Gestionnaire gestionnaire;

    // ✅ Assurer que le statut est toujours EN_ATTENTE par défaut
    @PrePersist
    public void prePersist() {
        if (this.statut == null) {
            this.statut = StatutDemande.EN_ATTENTE;
        }
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur demandeur) {
        this.demandeur = demandeur;
    }

    public Utilisateur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Utilisateur chauffeur) {
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

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public Gestionnaire getGestionnaire() {
        return gestionnaire;
    }

    public void setGestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;
    }
}
