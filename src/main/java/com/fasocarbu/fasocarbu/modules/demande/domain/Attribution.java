package com.fasocarbu.fasocarbu.modules.demande.domain;

import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import com.fasocarbu.fasocarbu.modules.user.domain.Chauffeur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
}
