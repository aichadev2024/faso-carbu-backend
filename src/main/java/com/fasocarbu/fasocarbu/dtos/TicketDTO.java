package com.fasocarbu.fasocarbu.dtos;

import com.fasocarbu.fasocarbu.enums.StatutTicket;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {
    private Long id;
    private LocalDateTime dateEmission;
    private LocalDateTime dateValidation;
    private BigDecimal montant;
    private BigDecimal quantite;
    private StatutTicket statut;
    private String codeQr;

    // Infos utilisateur (chauffeur)
    private String utilisateurNom;
    private String utilisateurPrenom;

    // Infos validateur (agent station)
    private String validateurNom;
    private String validateurPrenom;

    // Infos station
    private String stationNom;

    // Infos véhicule
    private String vehiculeImmatriculation;

    // Infos carburant
    private String carburantNom;

    // ✅ Ajout entrepriseId
    private Long entrepriseId;

    // ---------- CONSTRUCTEURS ----------
    public TicketDTO() {
    }

    public TicketDTO(com.fasocarbu.fasocarbu.models.Ticket ticket) {
        this.id = ticket.getId();
        this.dateEmission = ticket.getDateEmission();
        this.dateValidation = ticket.getDateValidation();
        this.montant = ticket.getMontant();
        this.quantite = ticket.getQuantite();
        this.statut = ticket.getStatut();
        this.codeQr = ticket.getCodeQr();

        if (ticket.getUtilisateur() != null) {
            this.utilisateurNom = ticket.getUtilisateur().getNom();
            this.utilisateurPrenom = ticket.getUtilisateur().getPrenom();
        }

        if (ticket.getValidateur() != null) {
            this.validateurNom = ticket.getValidateur().getNom();
            this.validateurPrenom = ticket.getValidateur().getPrenom();
        }

        if (ticket.getStation() != null) {
            this.stationNom = ticket.getStation().getNom();

            // ✅ Récupération entrepriseId via adminStation
            if (ticket.getStation().getAdminStation() != null &&
                    ticket.getStation().getAdminStation().getEntreprise() != null) {
                this.entrepriseId = ticket.getStation()
                        .getAdminStation()
                        .getEntreprise()
                        .getId();
            }
        }

        if (ticket.getVehicule() != null) {
            this.vehiculeImmatriculation = ticket.getVehicule().getImmatriculation();
        }

        if (ticket.getCarburant() != null) {
            this.carburantNom = ticket.getCarburant().getNom();
        }
    }

    // ---------- GETTERS & SETTERS ----------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDateTime dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public StatutTicket getStatut() {
        return statut;
    }

    public void setStatut(StatutTicket statut) {
        this.statut = statut;
    }

    public String getCodeQr() {
        return codeQr;
    }

    public void setCodeQr(String codeQr) {
        this.codeQr = codeQr;
    }

    public String getUtilisateurNom() {
        return utilisateurNom;
    }

    public void setUtilisateurNom(String utilisateurNom) {
        this.utilisateurNom = utilisateurNom;
    }

    public String getUtilisateurPrenom() {
        return utilisateurPrenom;
    }

    public void setUtilisateurPrenom(String utilisateurPrenom) {
        this.utilisateurPrenom = utilisateurPrenom;
    }

    public String getValidateurNom() {
        return validateurNom;
    }

    public void setValidateurNom(String validateurNom) {
        this.validateurNom = validateurNom;
    }

    public String getValidateurPrenom() {
        return validateurPrenom;
    }

    public void setValidateurPrenom(String validateurPrenom) {
        this.validateurPrenom = validateurPrenom;
    }

    public String getStationNom() {
        return stationNom;
    }

    public void setStationNom(String stationNom) {
        this.stationNom = stationNom;
    }

    public String getVehiculeImmatriculation() {
        return vehiculeImmatriculation;
    }

    public void setVehiculeImmatriculation(String vehiculeImmatriculation) {
        this.vehiculeImmatriculation = vehiculeImmatriculation;
    }

    public String getCarburantNom() {
        return carburantNom;
    }

    public void setCarburantNom(String carburantNom) {
        this.carburantNom = carburantNom;
    }

    public Long getEntrepriseId() {
        return entrepriseId;
    }

    public void setEntrepriseId(Long entrepriseId) {
        this.entrepriseId = entrepriseId;
    }
}
