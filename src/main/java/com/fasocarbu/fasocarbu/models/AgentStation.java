package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent_station")
public class AgentStation extends Utilisateur {

    public AgentStation() {
        super();
        this.setRole("AGENTSTATION");  // Définit automatiquement le rôle
    }

    @Override
    public void initialiserProfil() {
        // Ici tu peux initialiser des valeurs spécifiques à l'agent de station
        // Exemple : initialiser des champs spécifiques ou des permissions
    }

    // Ajoute ici des attributs et méthodes spécifiques à AgentStation si besoin
}
