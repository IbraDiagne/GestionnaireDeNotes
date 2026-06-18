package com.example.gestionnairedesnotes;

import com.example.gestionnairedesnotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteFilter {

    // Filtre les notes par titre (insensible à la casse)
    public static List<Note> filterByTitle(List<Note> notes, String query) {
        if (query == null || query.trim().isEmpty()) {
            return notes;
        }
        List<Note> resultat = new ArrayList<>();
        String queryLower = query.toLowerCase();
        for (Note note : notes) {
            if (note.getTitre().toLowerCase().contains(queryLower)) {
                resultat.add(note);
            }
        }
        return resultat;
    }

    // Filtre uniquement les notes favorites
    public static List<Note> filterByFavori(List<Note> notes) {
        List<Note> resultat = new ArrayList<>();
        for (Note note : notes) {
            if (note.isFavori()) {
                resultat.add(note);
            }
        }
        return resultat;
    }

    // Combine les deux filtres : d'abord favoris, puis recherche par titre
    public static List<Note> filterCombined(List<Note> notes, String query, boolean favoriOnly) {
        List<Note> resultat = notes;
        if (favoriOnly) {
            resultat = filterByFavori(resultat);
        }
        resultat = filterByTitle(resultat, query);
        return resultat;
    }
}