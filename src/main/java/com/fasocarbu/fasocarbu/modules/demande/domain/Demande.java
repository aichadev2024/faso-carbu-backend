package com.fasocarbu.fasocarbu.modules.demande.domain;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.user.domain.Gestionnaire;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @PrePersist
    public void prePersist() {
        if (this.statut == null) {
            this.statut = StatutDemande.EN_ATTENTE;
        }
    }
}
