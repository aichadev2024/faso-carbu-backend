package com.fasocarbu.fasocarbu.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_station;

    @Column(nullable = false)
    private String nom = "Inconnu";

    @Column(nullable = false)
    private String adresse = "";

    @Column(nullable = false)
    private String ville = "";

    @Column(nullable = false)
    private String statut = "inactif";

    @OneToOne(mappedBy = "station", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({ "station", "carburants" })
    private AdminStation adminStation;

    public long getId() {
        return id_station;
    }

    public void setId(long id) {
        this.id_station = id;
    }

    public String getNom() {
        return nom != null ? nom : "Inconnu";
    }

    public void setNom(String nom) {
        this.nom = nom != null ? nom : "Inconnu";
    }

    public String getAdresse() {
        return adresse != null ? adresse : "";
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse != null ? adresse : "";
    }

    public String getVille() {
        return ville != null ? ville : "";
    }

    public void setVille(String ville) {
        this.ville = ville != null ? ville : "";
    }

    public String getStatut() {
        return statut != null ? statut : "inactif";
    }

    public void setStatut(String statut) {
        this.statut = statut != null ? statut : "inactif";
    }

    public AdminStation getAdminStation() {
        return adminStation;
    }

    public void setAdminStation(AdminStation adminStation) {
        this.adminStation = adminStation;
    }
}
