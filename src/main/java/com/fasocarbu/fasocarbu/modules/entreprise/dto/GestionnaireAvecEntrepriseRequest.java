package com.fasocarbu.fasocarbu.modules.entreprise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GestionnaireAvecEntrepriseRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String telephone;
    private String nomEntreprise;
}
