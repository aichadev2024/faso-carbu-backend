package com.fasocarbu.fasocarbu.modules.demande.dto;

import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DemandeResponse {
    private String id;
    private double quantite;
    private String statut;

    private String demandeurNom;
    private String demandeurPrenom;

    private String gestionnaireNom;
    private String gestionnairePrenom;

    private String stationNom;
    private String vehiculeImmatriculation;
    private String carburantNom;

    private Long entrepriseId;

    public DemandeResponse(Demande demande) {
        this.id = demande.getId() != null ? demande.getId().toString() : null;
        this.quantite = demande.getQuantite();
        this.statut = demande.getStatut() != null ? demande.getStatut().name() : null;

        if (demande.getDemandeur() != null) {
            this.demandeurNom = demande.getDemandeur().getNom();
            this.demandeurPrenom = demande.getDemandeur().getPrenom();
        }

        if (demande.getGestionnaire() != null) {
            this.gestionnaireNom = demande.getGestionnaire().getNom();
            this.gestionnairePrenom = demande.getGestionnaire().getPrenom();
        }

        if (demande.getStation() != null) {
            this.stationNom = demande.getStation().getNom();

            if (demande.getStation().getAdminStation() != null &&
                    demande.getStation().getAdminStation().getEntreprise() != null) {
                this.entrepriseId = demande.getStation()
                        .getAdminStation()
                        .getEntreprise()
                        .getId();
            }
        }

        if (demande.getVehicule() != null) {
            this.vehiculeImmatriculation = demande.getVehicule().getImmatriculation();
        }

        if (demande.getCarburant() != null) {
            this.carburantNom = demande.getCarburant().getNom();
        }
    }
}
