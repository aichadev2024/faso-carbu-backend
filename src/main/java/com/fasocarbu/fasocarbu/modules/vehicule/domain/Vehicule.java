package com.fasocarbu.fasocarbu.modules.vehicule.domain;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_vehicule;

    @ManyToOne
    @JoinColumn(name = "id_entreprise")
    private Entreprise entreprise;

    private String marque;
    private String modele;
    private String immatriculation;

    @Column
    private double quotaCarburant;

    @ManyToOne
    @JoinColumn(name = "carburant_id")
    private Carburant carburant;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    public Vehicule(String marque, String modele, String immatriculation, String typecarburant, Utilisateur utilisateur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.utilisateur = utilisateur;
    }

    public long getId() {
        return id_vehicule;
    }

    public void setId(long id) {
        this.id_vehicule = id;
    }
}
