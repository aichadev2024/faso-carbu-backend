package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Gestionnaire extends Utilisateur {


    public Gestionnaire() {
        super();
    }

    public Gestionnaire(String nom, String prenom, String email, String motDePasse, int niveauAcces) {
        super(nom, prenom, email, motDePasse);
        this.initialiserProfil(); // 👈 définit automatiquement le rôle
    }

    @Override
    public void initialiserProfil() {
        super.setRole(String.valueOf(Role.GESTIONNAIRE));
    }
}
