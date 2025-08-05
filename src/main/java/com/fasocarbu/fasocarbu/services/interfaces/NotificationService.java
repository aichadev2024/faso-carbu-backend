package com.fasocarbu.fasocarbu.service;

import java.util.List;

public interface NotificationService {
    void sendNotification(List<String> fcmTokens, String title, String body);
}
