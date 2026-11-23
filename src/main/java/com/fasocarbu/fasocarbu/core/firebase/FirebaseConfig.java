package com.fasocarbu.fasocarbu.core.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Initialise Firebase (notifications push FCM).
 * FIREBASE_CONFIG_JSON peut contenir soit le JSON du compte de service,
 * soit le chemin vers ce fichier JSON. Sans configuration valide,
 * l'application démarre quand même, avec les notifications push désactivées.
 */
@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CONFIG_JSON:}")
    private String firebaseConfig;

    @PostConstruct
    void init() {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }
        if (firebaseConfig == null || firebaseConfig.isBlank()) {
            log.warn("FIREBASE_CONFIG_JSON absent : notifications push désactivées.");
            return;
        }
        try (InputStream credentials = openCredentials(firebaseConfig.trim())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentials))
                    .build();
            FirebaseApp.initializeApp(options);
            log.info("Firebase initialisé.");
        } catch (Exception e) {
            log.warn("Configuration Firebase invalide ({}) : notifications push désactivées.", e.getMessage());
        }
    }

    private static InputStream openCredentials(String value) throws Exception {
        if (value.startsWith("{")) {
            return new ByteArrayInputStream(value.replace("\\n", "\n").getBytes(StandardCharsets.UTF_8));
        }
        return Files.newInputStream(Path.of(value));
    }
}
