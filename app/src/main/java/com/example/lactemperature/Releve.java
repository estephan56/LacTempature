package com.example.lactemperature;

public class Releve {
    protected String jour;
    protected String mois;
    protected String temperature6h;
    protected String temperature12h;
    protected String temperature18h;
    protected String temperature24h;
    protected String id_lac;

    public Releve(String jour, String mois, String temperature6h, String temperature12h, String temperature18h, String temperature24h, String id_lac) {
        this.jour = jour;
        this.mois = mois;
        this.temperature6h = temperature6h;
        this.temperature12h = temperature12h;
        this.temperature18h = temperature18h;
        this.temperature24h = temperature24h;
        this.id_lac = id_lac;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public String getTemperature6h() {
        return temperature6h;
    }

    public void setTemperature6h(String temperature6h) {
        this.temperature6h = temperature6h;
    }

    public String getTemperature12h() {
        return temperature12h;
    }

    public void setTemperature12h(String temperature12h) {
        this.temperature12h = temperature12h;
    }

    public String getTemperature18h() {
        return temperature18h;
    }

    public void setTemperature18h(String temperature18h) {
        this.temperature18h = temperature18h;
    }

    public String getTemperature24h() {
        return temperature24h;
    }

    public void setTemperature24h(String temperature24h) {
        this.temperature24h = temperature24h;
    }

    public String getId_lac() {
        return id_lac;
    }

    public void setId_lac(String id_lac) {
        this.id_lac = id_lac;
    }
}
