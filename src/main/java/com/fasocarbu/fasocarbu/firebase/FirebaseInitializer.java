package com.fasocarbu.fasocarbu.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount =
                getClass().getClassLoader().getResourceAsStream("firebase-config.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialisé !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Erreur d'initialisation Firebase : " + e.getMessage());
        }
    }
}
