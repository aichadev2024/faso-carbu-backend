package com.fasocarbu.fasocarbu.controllers;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final String BREVO_API_KEY = System.getenv("BREVO_API_KEY");
    private final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    @PostMapping("/send-reset")
    public ResponseEntity<String> sendResetCode(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String code = payload.get("code");

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", BREVO_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
                    {
                      "sender": {"name": "FasoCarbu", "email": "contact@fasocarbu.com"},
                      "to": [{"email": "%s"}],
                      "subject": "Code de réinitialisation FasoCarbu",
                      "htmlContent": "<p>Bonjour, voici votre code de réinitialisation : <strong>%s</strong></p>"
                    }
                    """.formatted(email, code);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, entity, String.class);

            return ResponseEntity.status(response.getStatusCode()).body("Email envoyé avec succès ✅");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur d’envoi : " + e.getMessage());
        }
    }
}
