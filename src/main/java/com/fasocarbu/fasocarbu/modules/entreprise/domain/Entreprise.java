package com.fasocarbu.fasocarbu.modules.entreprise.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;
    private String adresse;

    public Entreprise(String nom) {
        this.nom = nom;
    }

    public Entreprise(Long id) {
        this.id = id;
    }
}
