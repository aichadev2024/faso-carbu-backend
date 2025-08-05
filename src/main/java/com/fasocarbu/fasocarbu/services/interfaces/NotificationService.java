package com.fasocarbu.fasocarbu.services.interfaces;

import com.fasocarbu.fasocarbu.models.Demande;

public interface NotificationService {

    void notifierGestionnairesNouvelleDemande(Demande demande);

    void sendNotificationToGestionnaires(String title, String body);
}
