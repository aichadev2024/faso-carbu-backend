package com.fasocarbu.fasocarbu.dtos;

public class LoginRequest {
    private String email;
    private String motDePasse;
    private String tokenFcm;

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getTokenFcm() { return tokenFcm;}
    public void setTokenFcm(String tokenFcm){ this.tokenFcm = tokenFcm;}
}
