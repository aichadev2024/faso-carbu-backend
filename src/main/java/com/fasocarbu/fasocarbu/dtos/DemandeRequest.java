package com.fasocarbu.fasocarbu.dtos;


import java.util.UUID;

public class DemandeRequest {

    private Long demandeurId;
    private UUID gestionnaireId;
    private Long carburantId;
    private Long stationId;
    private Long vehiculeId;
    private double quantite;

    

    public Long getDemandeurId() {
        return demandeurId;
    }

    public void setDemandeurId(Long demandeurId) {
        this.demandeurId = demandeurId;
    }
    public UUID getGestionnaireId() {
    return gestionnaireId;
    }
    public void setGestionnaireId(UUID gestionnaireId){
        this.gestionnaireId = gestionnaireId;
    }
 

    public Long getCarburantId() {
        return carburantId;
    }

    public void setCarburantId(Long carburantId) {
        this.carburantId = carburantId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
