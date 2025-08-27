package com.fasocarbu.fasocarbu.models;

import com.fasocarbu.fasocarbu.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur {
    private static final Logger log = LoggerFactory.getLogger(Utilisateur.class);

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nom = "Inconnu";

    @Column(nullable = false)
    private String prenom = "Inconnu";

    private String telephone = "";

    @Column(nullable = false, unique = true)
    private String email = "inconnu@exemple.com";

    @Column(nullable = false)
    private String motDePasse = "";

    @Enumerated(EnumType.STRING)
    private Role role = Role.CHAUFFEUR; // valeur par défaut

    @Column(nullable = false)
    private Boolean actif = true;

    private String fcmToken = "";

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    public Utilisateur() {
    }

    public Utilisateur(String nom, String prenom, String telephone, String email, String motDePasse) {
        this.nom = nom != null ? nom : "Inconnu";
        this.prenom = prenom != null ? prenom : "Inconnu";
        this.telephone = telephone != null ? telephone : "";
        this.email = email != null ? email : "inconnu@exemple.com";
        this.motDePasse = motDePasse != null ? motDePasse : "";
    }

    // getters/setters avec sécurités null
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom != null ? nom : "Inconnu";
    }

    public void setNom(String nom) {
        this.nom = nom != null ? nom : "Inconnu";
    }

    public String getPrenom() {
        return prenom != null ? prenom : "Inconnu";
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom != null ? prenom : "Inconnu";
    }

    public String getEmail() {
        return email != null ? email : "inconnu@exemple.com";
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "inconnu@exemple.com";
    }

    public String getTelephone() {
        return telephone != null ? telephone : "";
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone != null ? telephone : "";
    }

    public String getMotDePasse() {
        return motDePasse != null ? motDePasse : "";
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse != null ? motDePasse : "";
    }

    public Role getRole() {
        return role != null ? role : Role.CHAUFFEUR;
    }

    public void setRole(String roleStr) {
        if (roleStr == null) {
            log.error("Rôle null reçu.");
            return;
        }
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(roleStr.trim())) {
                this.role = r;
                return;
            }
        }
        log.error("Rôle inconnu: '{}'. Aucun rôle défini.", roleStr);
    }

    public Boolean getActif() {
        return actif != null ? actif : true;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getFcmToken() {
        return fcmToken != null ? fcmToken : "";
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken != null ? fcmToken : "";
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public abstract void initialiserProfil();
}
