package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.Station;

public class StationDTO {
    private Long idStation;
    private String nom;
    private String adresse;
    private String ville;
    private String statut;

    public StationDTO() {
    }

    public StationDTO(Station station) {
        this.idStation = station.getId();
        this.nom = station.getNom();
        this.adresse = station.getAdresse();
        this.ville = station.getVille();
        this.statut = station.getStatut();
    }

    // getters et setters
    public Long getIdStation() {
        return idStation;
    }

    public void setIdStation(Long idStation) {
        this.idStation = idStation;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
