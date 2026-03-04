package com.fasocarbu.fasocarbu.dtos;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String motDePasse;
    private String role;
    private String nomUtilisateur; // ✅ Ajout du champ nomUtilisateur
}
