package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Chauffeur extends Utilisateur {

    private String permisConduite;

    // Constructeur sans argument requis par JPA
    public Chauffeur() {
        super();
    }

    public Chauffeur(String nom, String prenom, String email, String motDePasse, String permisConduite) {
        super(nom, prenom, email, motDePasse);
        this.permisConduite = permisConduite;
        this.initialiserProfil();
    }

    @Override
    public void initialiserProfil() {
        super.setRole(String.valueOf(Role.CHAUFFEUR));
    }

    // Getter et Setter pour permisConduite
    public String getPermisConduite() {
        return permisConduite;
    }

    public void setPermisConduite(String permisConduite) {
        this.permisConduite = permisConduite;
    }
}
