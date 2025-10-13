package com.fasocarbu.fasocarbu.dtos;

import java.util.UUID;

public class DemandeRequest {

    private UUID demandeurId;
    private UUID gestionnaireId;
    private UUID chauffeurId; // ✅ nouveau champ
    private Long carburantId;
    private Long stationId;
    private Long vehiculeId;
    private double quantite;

    public UUID getDemandeurId() {
        return demandeurId;
    }

    public void setDemandeurId(UUID demandeurId) {
        this.demandeurId = demandeurId;
    }

    public UUID getGestionnaireId() {
        return gestionnaireId;
    }

    public void setGestionnaireId(UUID gestionnaireId) {
        this.gestionnaireId = gestionnaireId;
    }

    public UUID getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(UUID chauffeurId) {
        this.chauffeurId = chauffeurId;
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
