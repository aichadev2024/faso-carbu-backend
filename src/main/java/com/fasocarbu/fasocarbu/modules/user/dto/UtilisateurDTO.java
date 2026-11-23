package com.fasocarbu.fasocarbu.modules.user.dto;

import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UtilisateurDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private String nomUtilisateur;

    public UtilisateurDTO(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.nom = utilisateur.getNom();
        this.prenom = utilisateur.getPrenom();
        this.email = utilisateur.getEmail();
        this.telephone = utilisateur.getTelephone();
        this.role = utilisateur.getRole() != null ? utilisateur.getRole().name() : null;
        this.nomUtilisateur = utilisateur.getNomUtilisateur();
    }
}
