package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "utilisateur_id")
@DiscriminatorValue("GESTIONNAIRE")
public class Gestionnaire extends Utilisateur {

    public Gestionnaire() {
        super();
    }

    public Gestionnaire(String nom, String prenom, String telephone, String email, String motDePasse, int niveauAcces) {
        super(nom, prenom, telephone, email, motDePasse);
        this.initialiserProfil(); // 👈 définit automatiquement le rôle
    }

    @Override
    public void initialiserProfil() {
        super.setRole(String.valueOf(Role.GESTIONNAIRE));
    }
}
