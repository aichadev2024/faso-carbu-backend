package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Consommation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "attribution_id", nullable = false)
    private Attribution attribution; // lien avec l'attribution

    @ManyToOne
    @JoinColumn(name = "carburant_id", nullable = false)
    private Carburant carburant; // type de carburant utilisé

    private LocalDateTime dateConsommation;

    private double quantiteUtilisee;

    private String commentaire; // facultatif

    // Getters et Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Attribution getAttribution() { return attribution; }
    public void setAttribution(Attribution attribution) { this.attribution = attribution; }

    public Carburant getCarburant() { return carburant; }
    public void setCarburant(Carburant carburant) { this.carburant = carburant; }

    public LocalDateTime getDateConsommation() { return dateConsommation; }
    public void setDateConsommation(LocalDateTime dateConsommation) { this.dateConsommation = dateConsommation; }

    public double getQuantiteUtilisee() { return quantiteUtilisee; }
    public void setQuantiteUtilisee(double quantiteUtilisee) { this.quantiteUtilisee = quantiteUtilisee; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
