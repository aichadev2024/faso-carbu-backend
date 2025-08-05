package com.fasocarbu.fasocarbu.dtos;

public class UpdateFcmTokenRequest {
    private Integer userId;
    private String fcmToken;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
