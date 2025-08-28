package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.models.Demande;

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

    // ✅ Ajout du champ entrepriseId
    private Long entrepriseId;

    public DemandeResponse(Demande demande) {
        this.id = demande.getId().toString();
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
                        .getId(); // ✅ plus de .toString()
            }
        }

        if (demande.getVehicule() != null) {
            this.vehiculeImmatriculation = demande.getVehicule().getImmatriculation();
        }

        if (demande.getCarburant() != null) {
            this.carburantNom = demande.getCarburant().getNom();
        }
    }

    // Getters nécessaires pour la sérialisation JSON
    public String getId() {
        return id;
    }

    public double getQuantite() {
        return quantite;
    }

    public String getStatut() {
        return statut;
    }

    public String getDemandeurNom() {
        return demandeurNom;
    }

    public String getDemandeurPrenom() {
        return demandeurPrenom;
    }

    public String getGestionnaireNom() {
        return gestionnaireNom;
    }

    public String getGestionnairePrenom() {
        return gestionnairePrenom;
    }

    public String getStationNom() {
        return stationNom;
    }

    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }

    public String getCarburantNom() {
        return carburantNom;
    }

    public Long getEntrepriseId() { // ✅ reste en Long
        return entrepriseId;
    }
}
