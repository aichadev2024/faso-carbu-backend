package com.fasocarbu.fasocarbu.modules.user.domain;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
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

    @Column(unique = true, nullable = false)
    private String nomUtilisateur;

    @Column(nullable = false, unique = true)
    private String email = "inconnu@exemple.com";

    @Column(nullable = false)
    private String motDePasse = "";

    @Enumerated(EnumType.STRING)
    private Role role = Role.CHAUFFEUR;

    @Column(nullable = false)
    private Boolean actif = true;

    private String fcmToken = "";

    @Column(name = "photo_profil")
    private String photoProfil;

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
        this.nomUtilisateur = email != null ? email.split("@")[0] : "user_" + UUID.randomUUID().toString().substring(0, 8);
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

    public void setRole(Role role) {
        this.role = role != null ? role : Role.CHAUFFEUR;
    }

    public abstract void initialiserProfil();
}
