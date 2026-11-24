package com.fasocarbu.fasocarbu.modules.user.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "utilisateur_id")
@DiscriminatorValue("GESTIONNAIRE")
public class Gestionnaire extends Utilisateur {

    public Gestionnaire() {
        super();
    }

    public Gestionnaire(String nom, String prenom, String telephone, String email, String motDePasse, int niveauAcces) {
        super(nom, prenom, telephone, email, motDePasse);
        this.initialiserProfil();
    }

    @Override
    public void initialiserProfil() {
        super.setRole(Role.GESTIONNAIRE);
    }
}
