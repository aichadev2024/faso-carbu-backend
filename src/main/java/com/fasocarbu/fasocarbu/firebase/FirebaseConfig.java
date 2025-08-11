package com.fasocarbu.fasocarbu.firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


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

    // Remplace les séquences \n par de vrais retours à la ligne
    firebaseConfigJson = firebaseConfigJson.replace("\\n", "\n");

    try (ByteArrayInputStream serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8))) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp app;
        if (FirebaseApp.getApps().isEmpty()) {
            app = FirebaseApp.initializeApp(options);
        } else {
            app = FirebaseApp.getApps().get(0);
        }

        return FirebaseMessaging.getInstance(app);
    }
}

}