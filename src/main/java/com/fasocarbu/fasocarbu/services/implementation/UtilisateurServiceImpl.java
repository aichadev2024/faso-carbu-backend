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
import org.springframework.web.multipart.MultipartFile;

import com.fasocarbu.fasocarbu.enums.Role;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private JavaMailSender mailSender;

    // 🟢 Map temporaire pour stocker les tokens (clé=email, valeur=code OTP)
    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

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
                if (registerRequest.getNomEntreprise() == null || registerRequest.getNomEntreprise().isEmpty()) {
                    throw new RuntimeException("Le nom de l'entreprise est obligatoire pour un gestionnaire");
                }
                if (registerRequest.getAdresseEntreprise() == null
                        || registerRequest.getAdresseEntreprise().isEmpty()) {
                    throw new RuntimeException("L'adresse de l'entreprise est obligatoire pour un gestionnaire");
                }

                Entreprise entreprise = new Entreprise();
                entreprise.setNom(registerRequest.getNomEntreprise());
                entreprise.setAdresse(registerRequest.getAdresseEntreprise());
                entreprise = entrepriseRepository.save(entreprise);

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
        Utilisateur gestionnaire = getUtilisateurByEmail(emailGestionnaire);
        if (gestionnaire == null || gestionnaire.getEntreprise() == null) {
            throw new RuntimeException("Gestionnaire ou entreprise non trouvée");
        }

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

        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(roleStr);
        utilisateur.setEntreprise(gestionnaire.getEntreprise());
        utilisateur.setActif(true);

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public List<Utilisateur> getChauffeursByEntreprise(Long entrepriseId) {
        return utilisateurRepository.findByEntreprise_IdAndRole(entrepriseId, Role.CHAUFFEUR);
    }

    @Override
    public void demanderResetMotDePasse(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        resetTokens.put(email, code);

        // ✅ Envoi du mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Réinitialisation de votre mot de passe - FasoCarbu");
        message.setText("Bonjour " + user.getPrenom() + ",\n\nVoici votre code de réinitialisation : "
                + code + "\n\nCe code est valable 10 minutes.");

        mailSender.send(message);
    }

    @Override
    public void resetMotDePasse(String email, String code, String nouveauMotDePasse) {
        String savedCode = resetTokens.get(email);
        if (savedCode == null || !savedCode.equals(code)) {
            throw new RuntimeException("Code invalide ou expiré");
        }

        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(user);

        resetTokens.remove(email);
    }

    @Override
    public String uploadPhotoProfil(UUID id, MultipartFile file) throws IOException {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Générer un nom unique pour le fichier
        String fileName = "profil_" + id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // ⚡ Dossier temporaire compatible Render
        Path uploadPath = Paths.get(System.getProperty("java.io.tmpdir"), "uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        // Copier le fichier
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // URL publique pour Flutter
        String photoUrl = "https://faso-carbu-backend-2.onrender.com/api/utilisateurs/uploads/" + fileName;

        utilisateur.setPhotoProfil(photoUrl);
        utilisateurRepository.save(utilisateur);

        return photoUrl;
    }

}
