package com.fasocarbu.fasocarbu.modules.notification.service;

import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;
import com.fasocarbu.fasocarbu.modules.user.domain.Role;
import com.fasocarbu.fasocarbu.modules.user.domain.Utilisateur;
import com.fasocarbu.fasocarbu.modules.user.repository.UtilisateurRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public void notifierGestionnairesNouvelleDemande(Demande demande) {
        String title = "Nouvelle demande";
        String body = "Une nouvelle demande a été créée par " + demande.getDemandeur().getNom();
        sendNotificationToUtilisateur(title, body);
    }

    @Override
    public void sendNotificationToUtilisateur(String title, String body) {
        List<Utilisateur> gestionnaires = utilisateurRepository.findByRole(Role.GESTIONNAIRE);

        for (Utilisateur g : gestionnaires) {
            if (g.getFcmToken() != null) {
                Message message = Message.builder()
                        .setToken(g.getFcmToken())
                        .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                        .build();
                try {
                    FirebaseMessaging.getInstance().send(message);
                } catch (Exception e) {
                    System.out.println("Erreur d’envoi à " + g.getNom() + ": " + e.getMessage());
                }
            }
        }
    }
}
