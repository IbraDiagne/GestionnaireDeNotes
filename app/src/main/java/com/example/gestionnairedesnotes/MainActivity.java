package com.example.gestionnairedesnotes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionnairedesnotes.adapter.NotesAdapter;
import com.example.gestionnairedesnotes.database.NotesDatabaseHelper;
import com.example.gestionnairedesnotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerNotes;
    private NotesAdapter adapter;
    private NotesDatabaseHelper dbHelper;
    private List<Note> toutesLesNotes = new ArrayList<>();
    private TextView textAucuneNote;
    private EditText editRecherche;
    private boolean afficherFavorisUniquement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiserVues();
        initialiserRecyclerView();
        initialiserRecherche();
        initialiserBoutonFavoris();
        chargerLesNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chargerLesNotes();
    }

    private void initialiserVues() {
        dbHelper = new NotesDatabaseHelper(this);
        textAucuneNote = findViewById(R.id.textAucuneNote);
        editRecherche = findViewById(R.id.editRecherche);
        recyclerNotes = findViewById(R.id.recyclerNotes);
    }

    private void initialiserRecyclerView() {
        recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(this, toutesLesNotes, new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onSimpleClick(Note note) {
                // Géré par le membre responsable de la modification
            }

            @Override
            public void onDoubleClick(Note note) {
                // Géré par le membre responsable des favoris
            }
        });
        recyclerNotes.setAdapter(adapter);
    }

    private void initialiserRecherche() {
        editRecherche.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrerNotes(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void initialiserBoutonFavoris() {
        Button btnFavoris = findViewById(R.id.btnFavoris);
        btnFavoris.setOnClickListener(v -> {
            afficherFavorisUniquement = !afficherFavorisUniquement;
            btnFavoris.setAlpha(afficherFavorisUniquement ? 0.6f : 1f);
            filtrerNotes(editRecherche.getText().toString());
        });
    }

    private void chargerLesNotes() {
        toutesLesNotes = dbHelper.obtenirToutesLesNotes();
        filtrerNotes(editRecherche.getText().toString());
    }

    private void filtrerNotes(String recherche) {
        List<Note> notesFiltrees = new ArrayList<>();
        for (Note note : toutesLesNotes) {
            boolean correspondRecherche = note.getTitre().toLowerCase()
                    .contains(recherche.toLowerCase());
            boolean correspondFavori = !afficherFavorisUniquement || note.isFavori();
            if (correspondRecherche && correspondFavori) {
                notesFiltrees.add(note);
            }
        }
        adapter.mettreAJourListe(notesFiltrees);
        textAucuneNote.setVisibility(notesFiltrees.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerNotes.setVisibility(notesFiltrees.isEmpty() ? View.GONE : View.VISIBLE);
    }
}