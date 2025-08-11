package com.fasocarbu.fasocarbu.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws Exception {
        // Lire le contenu JSON depuis la variable d'environnement
        String firebaseConfig = System.getenv("FIREBASE_CONFIG");

        if (firebaseConfig == null) {
            throw new IllegalStateException("❌ Variable d'environnement FIREBASE_CONFIG non définie !");
        }

        // Charger les credentials à partir de la chaîne JSON
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ByteArrayInputStream(firebaseConfig.getBytes(StandardCharsets.UTF_8))
        );

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Initialiser Firebase si ce n’est pas déjà fait
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseMessaging.getInstance();
    }
}
