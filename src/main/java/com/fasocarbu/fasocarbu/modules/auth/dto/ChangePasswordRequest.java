package com.fasocarbu.fasocarbu.modules.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {
    private String ancienMotDePasse;
    private String nouveauMotDePasse;
}
