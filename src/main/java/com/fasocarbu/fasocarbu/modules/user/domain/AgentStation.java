package com.fasocarbu.fasocarbu.modules.user.domain;

import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "agent_station")
@PrimaryKeyJoinColumn(name = "id_utilisateur")
@DiscriminatorValue("AGENT_STATION")
public class AgentStation extends Utilisateur {

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public AgentStation() {
        super();
        this.setRole(Role.AGENT_STATION);
    }

    @Override
    public void initialiserProfil() {
        this.setRole(Role.AGENT_STATION);
    }
}
