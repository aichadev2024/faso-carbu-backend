package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.Utilisateur;
import lombok.Data;
import java.util.UUID;

@Data
public class UtilisateurDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;

    // ✅ Constructeur à partir de l’entité
    public UtilisateurDTO(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.nom = utilisateur.getNom();
        this.prenom = utilisateur.getPrenom();
        this.email = utilisateur.getEmail();
        this.telephone = utilisateur.getTelephone();
        this.role = utilisateur.getRole().name(); // ex: "ROLE_CHAUFFEUR"
    }
}
