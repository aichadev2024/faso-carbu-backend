package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Override
    public void enregistrerUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
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
                utilisateur = new Gestionnaire();
                break;
            case "CHAUFFEUR":
                utilisateur = new Chauffeur();
                break;
            case "AGENT_STATION":
                utilisateur = new AgentStation();
                break;
            case "DEMANDEUR":
                utilisateur = new Demandeur(); // pas besoin de classe spéciale
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
}
