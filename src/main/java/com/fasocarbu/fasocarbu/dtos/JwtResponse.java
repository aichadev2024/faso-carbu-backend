package com.fasocarbu.fasocarbu.dtos;

import java.util.UUID;

public class JwtResponse {
    private String token;
    private UUID id;
    private String email;
    private String role;
    private String nom;
    private String prenom;
    private String telephone;
    


   public JwtResponse(String token, UUID id, String email, String role, String nom, String prenom,String telephone) {
    this.token = token;
    this.id = id;
    this.email = email;
    this.role = role;
    this.nom = nom;
    this.prenom = prenom;
    this.telephone = telephone;
}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
     public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
     public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getTelephone(){
        return telephone;
    }
    public void setTelephone(String telephone){
        this.telephone=telephone;
    }
}
