package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.StatutAttribution;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Attribution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Chauffeur chauffeur; 

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket; 

    private LocalDate dateAttribution;

    private double quantite; 

    @Enumerated(EnumType.STRING)
    private StatutAttribution statutAttribution; 
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public LocalDate getDateAttribution() { return dateAttribution; }
    public void setDateAttribution(LocalDate dateAttribution) { this.dateAttribution = dateAttribution; }

    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }

    public StatutAttribution getStatutAttribution() { return statutAttribution; }
    public void setStatutAttribution(StatutAttribution statutAttribution) {
        this.statutAttribution = statutAttribution;
    }
}
