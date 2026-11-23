package com.fasocarbu.fasocarbu.modules.user.dto;

import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminStationDTO {
    private UUID id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Boolean actif;
    private String role;

    public AdminStationDTO(AdminStation admin) {
        this.id = admin.getId();
        this.nom = admin.getNom();
        this.prenom = admin.getPrenom();
        this.email = admin.getEmail();
        this.telephone = admin.getTelephone();
        this.actif = admin.getActif();
        this.role = admin.getRole() != null ? admin.getRole().name() : null;
    }
}
