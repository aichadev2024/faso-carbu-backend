package com.fasocarbu.fasocarbu.modules.notification.service;

import java.util.concurrent.CompletableFuture;

public interface FcmService {
    CompletableFuture<String> sendNotification(String token, String title, String body);
}
