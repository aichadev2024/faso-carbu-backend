package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.services.interfaces.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class FcmServiceImpl implements FcmService {

    @Override
    @Async
    public CompletableFuture<String> sendNotification(String token, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return CompletableFuture.completedFuture("Notification envoyée : " + response);

        } catch (Exception e) {
            return CompletableFuture.completedFuture("Erreur d'envoi : " + e.getMessage());
        }
    }
}
