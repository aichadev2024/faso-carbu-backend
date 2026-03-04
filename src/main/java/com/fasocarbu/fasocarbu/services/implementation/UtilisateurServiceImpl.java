package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.dtos.CreateUserRequest;
import com.fasocarbu.fasocarbu.dtos.RegisterRequest;
import com.fasocarbu.fasocarbu.enums.Role;
import com.fasocarbu.fasocarbu.models.*;
import com.fasocarbu.fasocarbu.repositories.EntrepriseRepository;
import com.fasocarbu.fasocarbu.repositories.GestionnaireRepository;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private GestionnaireRepository gestionnaireRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${brevo.api.key}")
    private String BREVO_API_KEY;

    @Value("${brevo.url}")
    private String BREVO_URL;

    @Value("${upload.dir:${java.io.tmpdir}/uploads}")
    private String uploadDir;

    // Map temporaire pour tokens OTP
    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    // =================== Utilisateurs ===================
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
                if (registerRequest.getNomEntreprise() == null || registerRequest.getNomEntreprise().isEmpty())
                    throw new RuntimeException("Le nom de l'entreprise est obligatoire");
                if (registerRequest.getAdresseEntreprise() == null || registerRequest.getAdresseEntreprise().isEmpty())
                    throw new RuntimeException("L'adresse de l'entreprise est obligatoire");

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
    public List<Utilisateur> getUtilisateursParGestionnaire(UUID gestionnaireId) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(gestionnaireId)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé"));

        Long entrepriseId = gestionnaire.getEntreprise().getId();
        return utilisateurRepository.findByEntreprise_Id(entrepriseId);
    }

    @Override
    public Long getEntrepriseIdFromUser(UUID userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (utilisateur.getEntreprise() != null)
            return utilisateur.getEntreprise().getId();
        if (utilisateur instanceof Gestionnaire gestionnaire && gestionnaire.getEntreprise() != null)
            return gestionnaire.getEntreprise().getId();

        throw new RuntimeException("Aucune entreprise associée à cet utilisateur");
    }

    @Override
    public void supprimerUtilisateur(UUID id) {
        if (utilisateurRepository.existsById(id))
            utilisateurRepository.deleteById(id);
    }

    @Override
    public void changerMotDePasse(String email, String ancien, String nouveau) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(ancien, utilisateur.getMotDePasse()))
            throw new RuntimeException("Ancien mot de passe incorrect");

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
        if (gestionnaire == null || gestionnaire.getEntreprise() == null)
            throw new RuntimeException("Gestionnaire ou entreprise non trouvée");

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

    // =================== Réinitialisation mot de passe ===================
    @Override
    public void demanderResetMotDePasse(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String code = String.valueOf((int) (Math.random() * 900000) + 100000);
        resetTokens.put(email, code);

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", BREVO_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
                    {
                      "sender": {"name": "FasoCarbu", "email": "diarrassoubaa505@gmail.com"},
                      "to": [{"email": "%s"}],
                      "subject": "Code de réinitialisation FasoCarbu",
                      "htmlContent": "<p>Bonjour %s, voici votre code de réinitialisation : <strong>%s</strong></p>"
                    }
                    """.formatted(user.getEmail(), user.getPrenom(), code);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(BREVO_URL, entity, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Erreur d’envoi de l’email : " + e.getMessage());
        }
    }

    @Override
    public void resetMotDePasse(String email, String code, String nouveauMotDePasse) {
        String savedCode = resetTokens.get(email);
        if (savedCode == null || !savedCode.equals(code))
            throw new RuntimeException("Code invalide ou expiré");

        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setMotDePasse(passwordEncoder.encode(nouveauMotDePasse));
        utilisateurRepository.save(user);
        resetTokens.remove(email);
    }

    // =================== Upload Photo Profil (stockage local) ===================
    @Override
    public String uploadPhotoProfil(UUID id, MultipartFile file) throws IOException {
        // Récupérer l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (file.isEmpty()) {
            throw new RuntimeException("Fichier vide");
        }

        // dossier de stockage
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // suppression éventuelle de l'ancienne photo locale
        String oldPhoto = utilisateur.getPhotoProfil();
        if (oldPhoto != null && oldPhoto.startsWith("/api/utilisateurs/uploads/")) {
            String oldFilename = oldPhoto.substring(oldPhoto.lastIndexOf('/') + 1);
            try {
                Files.deleteIfExists(uploadPath.resolve(oldFilename));
            } catch (IOException ignored) {
            }
        }

        // construire un nom unique
        String original = file.getOriginalFilename();
        String extension = "";
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID().toString() + extension;
        Path target = uploadPath.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        // URL renvoyée utilisable par le front
        String fileUrl = "/api/utilisateurs/uploads/" + filename;
        utilisateur.setPhotoProfil(fileUrl);
        utilisateurRepository.save(utilisateur);

        return fileUrl;
    }

}
