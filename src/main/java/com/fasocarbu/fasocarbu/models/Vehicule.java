package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity
public class Vehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_vehicule;

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

    public Vehicule() {
    }

    public Vehicule(String marque, String modele, String immatriculation, String typecarburant, Utilisateur utilisateur) {
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.utilisateur = utilisateur;
    }

    public long getId() { return id_vehicule; }
    public void setId(long id) { this.id_vehicule = id; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public Carburant getCarburant() {
        return carburant;
    }

    public void setCarburant(Carburant carburant) {
        this.carburant = carburant;
    }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
    public double getQuotaCarburant() {
    return quotaCarburant;
}

    public void setQuotaCarburant(double quotaCarburant) {
      this.quotaCarburant = quotaCarburant;
}

}
