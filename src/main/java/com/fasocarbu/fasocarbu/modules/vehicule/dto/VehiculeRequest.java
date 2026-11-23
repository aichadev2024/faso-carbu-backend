package com.fasocarbu.fasocarbu.modules.vehicule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehiculeRequest {

    @NotNull
    private String marque;

    @NotNull
    private String modele;

    @NotNull
    private String immatriculation;

    private double quotaCarburant;

    @NotNull
    private Long carburantId;
}
