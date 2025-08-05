package com.fasocarbu.fasocarbu.dtos;

import java.util.UUID;

public class UpdateFcmTokenRequest {
    private UUID userId;
    private String fcmToken;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
