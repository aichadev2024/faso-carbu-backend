package com.fasocarbu.fasocarbu.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private UUID id;
    private String email;
    private String role;
    private String nom;
    private String prenom;
    private String telephone;
}
