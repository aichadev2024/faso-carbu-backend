package com.fasocarbu.fasocarbu.modules.user.service;

import com.fasocarbu.fasocarbu.modules.auth.dto.RegisterRequest;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.dto.CreateUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurService {

    Utilisateur registerUser(RegisterRequest registerRequest) throws RuntimeException;

    Utilisateur enregistrerUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateurById(UUID id);

    void supprimerUtilisateur(UUID id);

    void changerMotDePasse(String email, String ancien, String nouveau);

    Optional<Utilisateur> findByEmail(String email);

    void updateFcmToken(UUID userId, String fcmToken);

    Utilisateur save(Utilisateur utilisateur);

    Utilisateur getUtilisateurByEmail(String email);

    Utilisateur creerUtilisateurParGestionnaire(CreateUserRequest request, String emailGestionnaire);

    List<Utilisateur> getChauffeursByEntreprise(Long entrepriseId);

    void demanderResetMotDePasse(String email);

    Long getEntrepriseIdFromUser(UUID userId);

    List<Utilisateur> getUtilisateursParGestionnaire(UUID gestionnaireId);

    void resetMotDePasse(String email, String code, String nouveauMotDePasse);

    String uploadPhotoProfil(UUID userId, MultipartFile file) throws IOException;
}
