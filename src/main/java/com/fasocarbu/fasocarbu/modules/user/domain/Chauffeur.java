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
@DiscriminatorValue("CHAUFFEUR")
public class Chauffeur extends Utilisateur {

    private String permisConduite;

    public Chauffeur() {
        super();
    }

    public Chauffeur(String nom, String prenom, String telephone, String email, String motDePasse, String permisConduite) {
        super(nom, prenom, telephone, email, motDePasse);
        this.permisConduite = permisConduite;
        this.initialiserProfil();
    }

    @Override
    public void initialiserProfil() {
        super.setRole(Role.CHAUFFEUR);
    }
}
