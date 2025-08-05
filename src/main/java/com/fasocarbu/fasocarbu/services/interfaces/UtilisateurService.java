package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.models.Utilisateur;

import java.util.List;
import java.util.Optional;


public interface UtilisateurService {
    
    /**
     * Enregistre un nouvel utilisateur à partir d'un DTO RegisterRequest.
     * @param registerRequest les données d'inscription
     * @return l'utilisateur enregistré
     * @throws RuntimeException si l'email est déjà utilisé
     */
    Utilisateur registerUser(RegisterRequest registerRequest) throws RuntimeException;
    void enregistrerUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateurById(Integer id);

    List<Utilisateur> getAllUtilisateurs();

    void supprimerUtilisateur(Integer id);
    
    void changerMotDePasse(String email, String ancien, String nouveau);
    Optional<Utilisateur> findByEmail(String email);

    void updateFcmToken(Long userId, String fcmToken);


}
