package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.models.Utilisateur;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UtilisateurService {

    /**
     * Enregistre un nouvel utilisateur à partir d'un DTO RegisterRequest.
     * 
     * @param registerRequest les données d'inscription
     * @return l'utilisateur enregistré
     * @throws RuntimeException si l'email est déjà utilisé
     */
    Utilisateur registerUser(RegisterRequest registerRequest) throws RuntimeException;

    Utilisateur enregistrerUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateurById(UUID id);

    List<Utilisateur> getAllUtilisateurs();

    void supprimerUtilisateur(UUID id);

    void changerMotDePasse(String email, String ancien, String nouveau);

    Optional<Utilisateur> findByEmail(String email);

    void updateFcmToken(UUID userId, String fcmToken);

    Utilisateur save(Utilisateur utilisateur);

    Utilisateur getUtilisateurByEmail(String email);

    Utilisateur creerUtilisateurParGestionnaire(CreateUserRequest request, String emailGestionnaire);

    List<Utilisateur> getChauffeursByEntreprise(Long entrepriseId);

    void demanderResetMotDePasse(String email);

    void resetMotDePasse(String email, String code, String nouveauMotDePasse);

    String uploadPhotoProfil(Long userId, MultipartFile file) throws IOException;
}
