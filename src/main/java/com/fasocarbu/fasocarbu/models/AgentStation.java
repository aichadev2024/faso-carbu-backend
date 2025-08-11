package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.*;

@Entity
@Table(name = "agent_station")
@PrimaryKeyJoinColumn(name = "id_utilisateur")
public class AgentStation extends Utilisateur {

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public AgentStation() {
        super();
        this.setRole("AGENT_STATION");
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public void initialiserProfil() {
        // Initialisation personnalisée ici
    }
}
