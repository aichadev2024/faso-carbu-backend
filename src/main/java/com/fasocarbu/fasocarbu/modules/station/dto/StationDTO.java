package com.fasocarbu.fasocarbu.modules.station.dto;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationDTO {
    private Long idStation;
    private String nom;
    private String adresse;
    private String ville;
    private String statut;

    public StationDTO(Station station) {
        this.idStation = station.getId();
        this.nom = station.getNom();
        this.adresse = station.getAdresse();
        this.ville = station.getVille();
        this.statut = station.getStatut();
    }
}
