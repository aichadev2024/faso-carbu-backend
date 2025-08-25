package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.Vehicule;
import java.util.UUID;

public class VehiculeDTO {
    private long id;
    private String marque;
    private String modele;
    private String immatriculation;
    private double quotaCarburant;
    private long carburantId;
    private String carburantNom;
    private UUID utilisateurId;
    private String utilisateurNom;

    // Constructeur depuis Vehicule
    public VehiculeDTO(Vehicule v) {
        this.id = v.getId();
        this.marque = v.getMarque() != null ? v.getMarque() : "Inconnu";
        this.modele = v.getModele() != null ? v.getModele() : "Inconnu";
        this.immatriculation = v.getImmatriculation() != null ? v.getImmatriculation() : "XXXX";
        this.quotaCarburant = v.getQuotaCarburant();

        this.carburantId = v.getCarburant() != null ? v.getCarburant().getId() : 0;
        this.carburantNom = v.getCarburant() != null && v.getCarburant().getNom() != null
                ? v.getCarburant().getNom()
                : "Inconnu";

        this.utilisateurId = v.getUtilisateur() != null ? v.getUtilisateur().getId() : null;
        this.utilisateurNom = v.getUtilisateur() != null && v.getUtilisateur().getNom() != null
                ? v.getUtilisateur().getNom()
                : "Inconnu";
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public double getQuotaCarburant() {
        return quotaCarburant;
    }

    public void setQuotaCarburant(double quotaCarburant) {
        this.quotaCarburant = quotaCarburant;
    }

    public long getCarburantId() {
        return carburantId;
    }

    public void setCarburantId(long carburantId) {
        this.carburantId = carburantId;
    }

    public String getCarburantNom() {
        return carburantNom;
    }

    public void setCarburantNom(String carburantNom) {
        this.carburantNom = carburantNom;
    }

    public UUID getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(UUID utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }
}
