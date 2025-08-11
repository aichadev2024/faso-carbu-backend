package com.fasocarbu.fasocarbu.models;

import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;



@Entity
@DiscriminatorValue("DEMANDEUR") 
public class Demandeur extends Utilisateur {

    // Constructeur
    public Demandeur() {
        super(); 
    }

    @Override
    public void initialiserProfil() {
        this.setRole("DEMANDEUR");

    
    }
}
