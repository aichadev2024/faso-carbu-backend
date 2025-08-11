package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity

public class Station {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id_station;
    private String nom;
    private String adresse;
    private String ville;
    @OneToOne(mappedBy = "station", cascade = CascadeType.ALL)
    private AdminStation adminStation;


    public long getId() {
        return id_station;
    }

    public void setId(long id) {
        this.id_station = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}
