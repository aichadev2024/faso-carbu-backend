package com.fasocarbu.fasocarbu.modules.notification.web;

import com.fasocarbu.fasocarbu.modules.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final FcmService fcmService;

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
