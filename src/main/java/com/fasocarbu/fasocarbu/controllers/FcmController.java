package com.fasocarbu.fasocarbu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.fasocarbu.fasocarbu.services.interfaces.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    @Autowired
    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String title = request.get("title");
            String body = request.get("body");

            fcmService.sendNotification(token, title, body);
            return ResponseEntity.ok("Notification envoyée !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi : " + e.getMessage());
        }
    }
}
