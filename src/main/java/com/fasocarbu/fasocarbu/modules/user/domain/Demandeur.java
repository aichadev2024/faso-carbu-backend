package com.fasocarbu.fasocarbu.modules.user.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("DEMANDEUR")
public class Demandeur extends Utilisateur {

    public Demandeur() {
        super();
    }

    @Override
    public void initialiserProfil() {
        this.setRole(Role.DEMANDEUR);
    }
}
