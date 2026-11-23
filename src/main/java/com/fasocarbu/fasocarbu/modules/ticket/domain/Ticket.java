package com.fasocarbu.fasocarbu.modules.ticket.domain;

import com.fasocarbu.fasocarbu.modules.demande.domain.Attribution;
import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_ticket;

    private LocalDateTime dateEmission;

    private LocalDateTime dateValidation;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "attribution_id")
    private Attribution attribution;

    private BigDecimal montant;

    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    private StatutTicket statut;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_validateur")
    private Utilisateur validateur;

    @ManyToOne
    @JoinColumn(name = "id_vehicule")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_station")
    private Station station;

    @Column(columnDefinition = "TEXT")
    private String codeQr;

    @OneToOne
    @JoinColumn(name = "demande_id", unique = true)
    private Demande demande;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    public long getId() {
        return id_ticket;
    }

    public void setId(long id) {
        this.id_ticket = id;
    }
}
