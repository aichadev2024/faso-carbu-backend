package com.fasocarbu.fasocarbu.security.services;

import com.fasocarbu.fasocarbu.models.Entreprise;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {
    private final Utilisateur utilisateur;

    public UserDetailsImpl(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
    }

    @Override
    public String getPassword() {
        return utilisateur.getMotDePasse();
    }

    @Override
    public String getUsername() {
        return utilisateur.getEmail();
    }

    public UUID getId() {
        return utilisateur.getId();
    }

    public String getNom() {
        return utilisateur.getNom();
    }

    public String getPrenom() {
        return utilisateur.getPrenom();
    }

    // 🔹 Getter pour l'entreprise
    public Entreprise getEntreprise() {
        return utilisateur.getEntreprise();
    }

    // 🔹 Getter pour l'id de l'entreprise
    public Long getEntrepriseId() {
        Entreprise e = utilisateur.getEntreprise();
        return e != null ? e.getId() : null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
