package com.fasocarbu.fasocarbu.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String motDePasse;

    @NotBlank
    private String role;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String telephone;

    private String nomUtilisateur;
    private String nomEntreprise;
    private String adresseEntreprise;
}
