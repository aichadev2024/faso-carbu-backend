package com.fasocarbu.fasocarbu.modules.notification.service;

import com.fasocarbu.fasocarbu.modules.demande.domain.Demande;

public interface NotificationService {

    void notifierGestionnairesNouvelleDemande(Demande demande);

    void sendNotificationToUtilisateur(String title, String body);

}
