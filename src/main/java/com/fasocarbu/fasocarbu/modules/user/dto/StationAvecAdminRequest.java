package com.fasocarbu.fasocarbu.modules.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StationAvecAdminRequest {
    private String nomStation;
    private String adresseStation;
    private String villeStation;

    private String nomAdmin;
    private String prenomAdmin;
    private String emailAdmin;
    private String motDePasseAdmin;
    private String telephoneAdmin;
    private String nomUtilisateurAdmin;
    private Long entrepriseId;
}
