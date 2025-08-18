package com.fasocarbu.fasocarbu.dtos;

public class StationAvecAdminRequest {

    private String nomStation;
    private String adresseStation;
    private String villeStation;

    private String nomAdmin;
    private String prenomAdmin;
    private String emailAdmin;
    private String motDePasseAdmin;
    private String telephoneAdmin;

    public String getNomStation() {
        return nomStation;
    }

    public void setNomStation(String nomStation) {
        this.nomStation = nomStation;
    }

    public String getAdresseStation() {
        return adresseStation;
    }

    public void setAdresseStation(String adresseStation) {
        this.adresseStation = adresseStation;
    }

    public String getVilleStation() {
        return villeStation;
    }

    public void setVilleStation(String villeStation) {
        this.villeStation = villeStation;
    }

    public String getNomAdmin() {
        return nomAdmin;
    }

    public void setNomAdmin(String nomAdmin) {
        this.nomAdmin = nomAdmin;
    }

    public String getPrenomAdmin() {
        return prenomAdmin;
    }

    public void setPrenomAdmin(String prenomAdmin) {
        this.prenomAdmin = prenomAdmin;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getMotDePasseAdmin() {
        return motDePasseAdmin;
    }

    public void setMotDePasseAdmin(String motDePasseAdmin) {
        this.motDePasseAdmin = motDePasseAdmin;
    }

    public String getTelephoneAdmin() {
        return telephoneAdmin;
    }

    public void setTelephoneAdmin(String telephoneAdmin) {
        this.telephoneAdmin = telephoneAdmin;
    }
}
