package com.fasocarbu.fasocarbu.modules.vehicule.dto;

import com.fasocarbu.fasocarbu.modules.vehicule.domain.Vehicule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeDTO {
    private long id;
    private String marque;
    private String modele;
    private String immatriculation;
    private double quotaCarburant;
    private long carburantId;
    private String carburantNom;
    private UUID utilisateurId;
    private String utilisateurNom;

    public VehiculeDTO(Vehicule v) {
        this.id = v.getId();
        this.marque = v.getMarque() != null ? v.getMarque() : "Inconnu";
        this.modele = v.getModele() != null ? v.getModele() : "Inconnu";
        this.immatriculation = v.getImmatriculation() != null ? v.getImmatriculation() : "XXXX";
        this.quotaCarburant = v.getQuotaCarburant();

        this.carburantId = v.getCarburant() != null ? v.getCarburant().getId() : 0;
        this.carburantNom = v.getCarburant() != null && v.getCarburant().getNom() != null
                ? v.getCarburant().getNom()
                : "Inconnu";

        this.utilisateurId = v.getUtilisateur() != null ? v.getUtilisateur().getId() : null;
        this.utilisateurNom = v.getUtilisateur() != null && v.getUtilisateur().getNom() != null
                ? v.getUtilisateur().getNom()
                : "Inconnu";
    }
}
