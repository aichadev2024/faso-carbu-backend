package com.fasocarbu.fasocarbu.modules.demande.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DemandeRequest {
    private UUID demandeurId;
    private UUID gestionnaireId;
    private UUID chauffeurId;
    private Long carburantId;
    private Long stationId;
    private Long vehiculeId;
    private double quantite;
}
