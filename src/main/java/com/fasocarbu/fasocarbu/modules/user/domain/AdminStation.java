package com.fasocarbu.fasocarbu.modules.user.domain;

import com.fasocarbu.fasocarbu.modules.station.domain.Carburant;
import com.fasocarbu.fasocarbu.modules.station.domain.Station;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN_STATION")
public class AdminStation extends Utilisateur {

    @OneToOne
    @JoinColumn(name = "station_id")
    @JsonIgnoreProperties({"adminStation"})
    private Station station;

    @OneToMany(mappedBy = "adminStation")
    @JsonIgnoreProperties({"adminStation", "station"})
    private List<Carburant> carburants;

    @Override
    public void initialiserProfil() {
        this.setRole(Role.ADMIN_STATION);
    }
}
