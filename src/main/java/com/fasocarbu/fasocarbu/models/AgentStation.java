package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@Table(name = "agent_station")
@PrimaryKeyJoinColumn(name = "id_utilisateur")
public class AgentStation extends Utilisateur {
    public AgentStation() {
        super();
        this.setRole("AGENT_STATION");
    }

    @Override
    public void initialiserProfil() {
        // Initialisation personnalisée ici
    }
}
