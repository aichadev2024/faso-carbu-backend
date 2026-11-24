package com.fasocarbu.fasocarbu.modules.consommation.domain;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Rapport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 2000)
    private String description;

    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    public Rapport() {
        this.dateCreation = LocalDateTime.now();
    }

    public Rapport(String titre, String description, Entreprise entreprise) {
        this.titre = titre;
        this.description = description;
        this.entreprise = entreprise;
        this.dateCreation = LocalDateTime.now();
    }
}
