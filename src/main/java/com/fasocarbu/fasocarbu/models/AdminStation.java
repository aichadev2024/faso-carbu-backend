package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class AdminStation extends Utilisateur {

    @OneToOne
    @JoinColumn(name = "station_id")
    @JsonIgnoreProperties("adminStation")
    private Station station;

    public AdminStation() {}

    @Override
    public void initialiserProfil() {
        
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
