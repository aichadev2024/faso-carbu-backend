package com.fasocarbu.fasocarbu.service.impl;

import com.fasocarbu.fasocarbu.service.NotificationService;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(List<String> fcmTokens, String title, String body) {
        for (String token : fcmTokens) {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("click_action", "FLUTTER_NOTIFICATION_CLICK") // pour Flutter
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Notification envoyée avec succès: " + response);
            } catch (FirebaseMessagingException e) {
                System.err.println("Erreur d’envoi de la notification: " + e.getMessage());
            }
        }
    }
}
