package com.fasocarbu.fasocarbu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasocarbu.fasocarbu.services.interfaces.FcmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
   @Autowired
    private final FcmService fcmService;

    public NotificationController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @RequestParam String token,
            @RequestParam String title,
            @RequestParam String body
    ) {
        try {
            fcmService.sendNotification(token, title, body);
            return ResponseEntity.ok("Notification envoyée !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }
}
