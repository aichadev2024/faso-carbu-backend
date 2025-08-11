package com.fasocarbu.fasocarbu.firebase;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        String firebaseConfigJson = System.getenv("FIREBASE_CONFIG_JSON");
        if (firebaseConfigJson == null) {
            throw new IllegalArgumentException("❌ Variable d’environnement FIREBASE_CONFIG_JSON non définie !");
        }

        // Création d’un fichier temporaire contenant la clé JSON
        Path tempFile = Files.createTempFile("firebase-service-account", ".json");
        Files.writeString(tempFile, firebaseConfigJson);

        try (FileInputStream serviceAccount = new FileInputStream(tempFile.toFile())) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp app = FirebaseApp.initializeApp(options);
            return FirebaseMessaging.getInstance(app);
        } finally {
            Files.deleteIfExists(tempFile);  // Supprime le fichier temporaire après usage
        }
    }
}
