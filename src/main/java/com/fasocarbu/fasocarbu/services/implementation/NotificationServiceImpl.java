package com.fasocarbu.fasocarbu.services.implementation;

import com.fasocarbu.fasocarbu.models.Demande;
import com.fasocarbu.fasocarbu.models.Utilisateur;
import com.fasocarbu.fasocarbu.repositories.UtilisateurRepository;
import com.fasocarbu.fasocarbu.services.interfaces.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasocarbu.fasocarbu.enums.Role;


import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public void notifierGestionnairesNouvelleDemande(Demande demande) {
        String title = "Nouvelle demande";
        String body = "Une nouvelle demande a été créée par " + demande.getChauffeur().getNom();
        sendNotificationToGestionnaires(title, body);
    }

   
     @Override
public void sendNotificationToGestionnaires(String title, String body) {
    
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
