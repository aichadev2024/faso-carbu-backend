package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class AdminStation extends Utilisateur {

    @OneToOne
    @JoinColumn(name = "station_id")
    @JsonIgnoreProperties("adminStation")
    private Station station;

    @OneToMany(mappedBy = "adminStation")
    private List<Carburant> carburants;

    public AdminStation() {
    }

    @Override
    public void initialiserProfil() {

    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public List<Carburant> getCarburants() {
        return carburants;
    }

    public void setCarburants(List<Carburant> carburants) {
        this.carburants = carburants;
    }
}
