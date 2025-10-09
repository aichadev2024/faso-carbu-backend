package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.AdminStation;
import java.util.UUID;

public class AdminStationDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Boolean actif;
    private String role;

    public AdminStationDTO() {
    }

    public AdminStationDTO(AdminStation admin) {
        this.id = admin.getId();
        this.nom = admin.getNom();
        this.prenom = admin.getPrenom();
        this.email = admin.getEmail();
        this.telephone = admin.getTelephone();
        this.actif = admin.getActif();
        this.role = admin.getRole().name();
    }

    // getters et setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
