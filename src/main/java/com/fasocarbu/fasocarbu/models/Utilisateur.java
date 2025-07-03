package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.Role;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Entity

@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Utilisateur{
    private static final Logger log = LoggerFactory.getLogger(Utilisateur.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_utilisateur;

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;

    }

    public Integer getId() { return id_utilisateur; }
    public void setId(Long id) { this.id_utilisateur= Math.toIntExact(id); }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }
    public void setRole(String role) {
        try {
            this.role = Role.valueOf(role.toUpperCase()); // Convertit en majuscules pour éviter la casse
        } catch (IllegalArgumentException e) {
            this.role = Role.AGENT_STATION; // Valeur par défaut si le rôle est invalide
            log.error("Rôle invalide: {}. Défaut à ROLE_ADMIN", role);
        }
    }

   public abstract void initialiserProfil();
}
