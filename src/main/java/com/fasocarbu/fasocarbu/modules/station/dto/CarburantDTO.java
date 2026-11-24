package com.fasocarbu.fasocarbu.modules.station.dto;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.user.dto.AdminStationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarburantDTO {
    private Long id;
    private String nom;
    private double prix;
    private StationDTO station;
    private AdminStationDTO adminStation;

    public CarburantDTO(Carburant carburant) {
        this.id = carburant.getId();
        this.nom = carburant.getNom();
        this.prix = carburant.getPrix() != null ? carburant.getPrix() : 0.0;

        if (carburant.getStation() != null) {
            this.station = new StationDTO(carburant.getStation());
        }

        if (carburant.getAdminStation() != null) {
            this.adminStation = new AdminStationDTO(carburant.getAdminStation());
        }
    }
}
