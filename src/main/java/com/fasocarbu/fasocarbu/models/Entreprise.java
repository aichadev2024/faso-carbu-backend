package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;
    private String adresse;

    public Entreprise() {}

    public Entreprise(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getAdresse(){
        return adresse;
    }
    public void setAdresse(String adresse){
        this.adresse= adresse;
    }
}
