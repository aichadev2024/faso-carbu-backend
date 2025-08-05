package com.fasocarbu.fasocarbu.services.interfaces;

import java.util.concurrent.CompletableFuture;

public interface FcmService {
    CompletableFuture<String> sendNotification(String token, String title, String body);
}
