package com.fasocarbu.fasocarbu.modules.station.domain;

import com.fasocarbu.fasocarbu.modules.entreprise.domain.Entreprise;
import com.fasocarbu.fasocarbu.modules.user.domain.AdminStation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_station;

    @Column(nullable = false)
    private String nom = "Inconnu";

    @Column(nullable = false)
    private String adresse = "";

    @Column(nullable = false)
    private String ville = "";

    @Column(nullable = false)
    private String statut = "inactif";

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    @OneToOne(mappedBy = "station", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"station", "carburants"})
    private AdminStation adminStation;

    public long getId() {
        return id_station;
    }

    public void setId(long id) {
        this.id_station = id;
    }
}
