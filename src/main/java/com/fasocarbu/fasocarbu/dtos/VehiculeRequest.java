package com.fasocarbu.fasocarbu.dtos;

import jakarta.validation.constraints.NotNull;

public class VehiculeRequest {

    @NotNull
    private String marque;

    @NotNull
    private String modele;

    @NotNull
    private String immatriculation;

    private double quotaCarburant;

    @NotNull
    private Long carburantId;

    // getters et setters
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

    public Long getCarburantId() {
        return carburantId;
    }

    public void setCarburantId(Long carburantId) {
        this.carburantId = carburantId;
    }
}
