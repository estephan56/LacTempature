package com.example.lactemperature;

public class Lac {
    protected String nom;
    protected String CoordonneesLat;
    protected String CoordonneesLong;

    public Lac(String nom, String coordonneesLat, String coordonneesLong) {
        this.nom = nom;
        CoordonneesLat = coordonneesLat;
        CoordonneesLong = coordonneesLong;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCoordonneesLat() {
        return CoordonneesLat;
    }

    public void setCoordonneesLat(String coordonneesLat) {
        CoordonneesLat = coordonneesLat;
    }

    public String getCoordonneesLong() {
        return CoordonneesLong;
    }

    public void setCoordonneesLong(String coordonneesLong) {
        CoordonneesLong = coordonneesLong;
    }
}
