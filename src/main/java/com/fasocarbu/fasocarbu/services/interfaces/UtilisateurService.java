package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Utilisateur;

import java.util.List;

public interface UtilisateurService {
    void enregistrerUtilisateur(Utilisateur utilisateur);
    Utilisateur getUtilisateurById(Integer id);
    List<Utilisateur> getAllUtilisateurs();
    void supprimerUtilisateur(Integer id);
}
