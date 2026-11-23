package com.fasocarbu.fasocarbu.modules.ticket.dto;

import com.fasocarbu.fasocarbu.modules.ticket.domain.StatutTicket;
import com.fasocarbu.fasocarbu.modules.ticket.domain.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private LocalDateTime dateEmission;
    private LocalDateTime dateValidation;
    private BigDecimal montant;
    private BigDecimal quantite;
    private StatutTicket statut;
    private String codeQr;

    private String utilisateurNom;
    private String utilisateurPrenom;

    private String validateurNom;
    private String validateurPrenom;

    private String stationNom;

    private String vehiculeImmatriculation;

    private String carburantNom;
    private Double carburantPrix;

    private Long entrepriseId;
    private String entrepriseNom;

    private BigDecimal somme;

    public TicketDTO(Ticket ticket) {
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
        }

        if (ticket.getVehicule() != null) {
            this.vehiculeImmatriculation = ticket.getVehicule().getImmatriculation();
        }

        if (ticket.getCarburant() != null) {
            this.carburantNom = ticket.getCarburant().getNom();
            this.carburantPrix = ticket.getCarburant().getPrix();

            if (this.quantite != null && this.carburantPrix != null) {
                this.somme = BigDecimal.valueOf(this.carburantPrix).multiply(this.quantite);
            }
        }

        if (ticket.getEntreprise() != null) {
            this.entrepriseId = ticket.getEntreprise().getId();
            this.entrepriseNom = ticket.getEntreprise().getNom();
        }
    }
}
