package com.fasocarbu.fasocarbu.dtos;

public class MotifRejetRequest {

    private String motif;

    public MotifRejetRequest() {
    }

    public MotifRejetRequest(String motif) {
        this.motif = motif;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
