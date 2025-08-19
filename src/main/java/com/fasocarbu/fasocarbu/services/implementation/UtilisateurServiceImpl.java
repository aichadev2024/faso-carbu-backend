package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.EntrepriseRepository;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Override
    public Utilisateur enregistrerUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur registerUser(RegisterRequest registerRequest) {
        if (utilisateurRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur;
        String roleStr = registerRequest.getRole().toUpperCase();

        switch (roleStr) {
            case "GESTIONNAIRE":
                // ✅ Vérification que le nom et l'adresse de l'entreprise sont fournis
                if (registerRequest.getNomEntreprise() == null || registerRequest.getNomEntreprise().isEmpty()) {
                    throw new RuntimeException("Le nom de l'entreprise est obligatoire pour un gestionnaire");
                }
                if (registerRequest.getAdresseEntreprise() == null
                        || registerRequest.getAdresseEntreprise().isEmpty()) {
                    throw new RuntimeException("L'adresse de l'entreprise est obligatoire pour un gestionnaire");
                }

                // Création de l'entreprise
                Entreprise entreprise = new Entreprise();
                entreprise.setNom(registerRequest.getNomEntreprise());
                entreprise.setAdresse(registerRequest.getAdresseEntreprise());
                entreprise = entrepriseRepository.save(entreprise);

                // Création du gestionnaire
                Gestionnaire gestionnaire = new Gestionnaire();
                gestionnaire.setEntreprise(entreprise);
                utilisateur = gestionnaire;
                break;

            case "CHAUFFEUR":
                utilisateur = new Chauffeur();
                break;
            case "AGENT_STATION":
                utilisateur = new AgentStation();
                break;
            case "DEMANDEUR":
                utilisateur = new Demandeur();
                break;
            default:
                throw new RuntimeException("Rôle inconnu : " + roleStr);
        }

        // Champs communs
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(registerRequest.getMotDePasse()));
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setTelephone(registerRequest.getTelephone());
        utilisateur.setRole(roleStr);
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur getUtilisateurById(UUID id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Override
    public void supprimerUtilisateur(UUID id) {
        if (utilisateurRepository.existsById(id)) {
            utilisateurRepository.deleteById(id);
        }
    }

    @Override
    public void changerMotDePasse(String email, String ancien, String nouveau) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(ancien, utilisateur.getMotDePasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(nouveau));
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public void updateFcmToken(UUID userId, String fcmToken) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        utilisateur.setFcmToken(fcmToken);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    @Override
    public Utilisateur creerUtilisateurParGestionnaire(CreateUserRequest request, String emailGestionnaire) {
        // Récupérer le gestionnaire connecté pour connaître son entreprise
        Utilisateur gestionnaire = getUtilisateurByEmail(emailGestionnaire);
        if (gestionnaire == null || gestionnaire.getEntreprise() == null) {
            throw new RuntimeException("Gestionnaire ou entreprise non trouvée");
        }

        // Créer l'utilisateur en fonction du rôle
        Utilisateur utilisateur;
        String roleStr = request.getRole().toUpperCase();
        switch (roleStr) {
            case "CHAUFFEUR":
                utilisateur = new Chauffeur();
                break;
            case "AGENT_STATION":
                utilisateur = new AgentStation();
                break;
            case "DEMANDEUR":
                utilisateur = new Demandeur();
                break;
            default:
                throw new RuntimeException("Rôle inconnu : " + roleStr);
        }

        // Remplir les champs communs
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(roleStr);
        utilisateur.setEntreprise(gestionnaire.getEntreprise());
        utilisateur.setActif(true);

        // Sauvegarder et retourner
        return utilisateurRepository.save(utilisateur);
    }

}
