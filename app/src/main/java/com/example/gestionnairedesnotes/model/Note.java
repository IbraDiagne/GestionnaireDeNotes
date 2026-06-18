package com.example.gestionnairedesnotes.model;

public class Note {
    private int id;
    private String titre;
    private String contenu;
    private String couleur;
    private boolean favori;
    private String date;

    public Note() {}

    public Note(int id, String titre, String contenu, String couleur, boolean favori, String date) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.couleur = couleur;
        this.favori = favori;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public boolean isFavori() { return favori; }
    public void setFavori(boolean favori) { this.favori = favori; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}