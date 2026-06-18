package com.example.gestionnairedesnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.gestionnairedesnotes.activity.NoteEditorActivity;
import com.example.gestionnairedesnotes.adapter.NotesAdapter;
import com.example.gestionnairedesnotes.database.NotesDatabaseHelper;
import com.example.gestionnairedesnotes.model.Note;

import java.util.List;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private NotesDatabaseHelper dbHelper;
    private TextView tvAucuneNotes;
    private EditText searchView;
    private Button btnFavoris;
    private FloatingActionButton fab;
    private LinearLayout paletteContainer;
    private boolean paletteVisible = false;
    private boolean favoriOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        tvAucuneNotes = findViewById(R.id.tvAucuneNotes);
        searchView = findViewById(R.id.searchView);
        btnFavoris = findViewById(R.id.btnFavoris);
        fab = findViewById(R.id.fab);
        paletteContainer = findViewById(R.id.paletteContainer);

        dbHelper = new NotesDatabaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(null);
        recyclerView.setAdapter(adapter);

        loadNotes();
        setupFab();
        setupPalette();
        setupSearch();
        setupBtnFavoris();
    }

    private void loadNotes() {
        List<Note> notes = dbHelper.getAllNotes();
        if (notes.isEmpty()) {
            tvAucuneNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvAucuneNotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.setNotes(notes);
    }
    private void filterNotes(String query, boolean favoriOnly) {
        List<Note> toutesLesNotes = dbHelper.getAllNotes();
        List<Note> notesFiltrees = NoteFilter.filterCombined(toutesLesNotes, query, favoriOnly);

        if (notesFiltrees.isEmpty()) {
            tvAucuneNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvAucuneNotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.setNotes(notesFiltrees);
    }

    private void setupSearch() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString(), favoriOnly);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBtnFavoris() {
        btnFavoris.setOnClickListener(v -> {
            favoriOnly = !favoriOnly;
            if (favoriOnly) {
                btnFavoris.setTextColor(Color.parseColor("#219653"));
            } else {
                btnFavoris.setTextColor(Color.parseColor("#000000"));
            }
            filterNotes(searchView.getText().toString(), favoriOnly);
        });
    }

    private void setupFab() {
        fab.setOnClickListener(v -> {
            if (paletteVisible) {
                paletteContainer.setVisibility(View.GONE);
                paletteVisible = false;
            } else {
                paletteContainer.setVisibility(View.VISIBLE);
                paletteVisible = true;
            }
        });
    }

    private void setupPalette() {
        ImageButton colorGreen = findViewById(R.id.colorGreen);
        ImageButton colorRed = findViewById(R.id.colorRed);
        ImageButton colorBlue = findViewById(R.id.colorBlue);
        ImageButton colorYellow = findViewById(R.id.colorYellow);
        ImageButton colorOrange = findViewById(R.id.colorOrange);
        ImageButton colorGray = findViewById(R.id.colorGray);

        colorGreen.setOnClickListener(v -> ouvrirEditeur("#219653"));
        colorRed.setOnClickListener(v -> ouvrirEditeur("#EB5757"));
        colorBlue.setOnClickListener(v -> ouvrirEditeur("#2F80ED"));
        colorYellow.setOnClickListener(v -> ouvrirEditeur("#F2C94C"));
        colorOrange.setOnClickListener(v -> ouvrirEditeur("#F2994A"));
        colorGray.setOnClickListener(v -> ouvrirEditeur("#828282"));
    }

    private void ouvrirEditeur(String couleur) {
        paletteContainer.setVisibility(View.GONE);
        paletteVisible = false;
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra("NOTE_COLOR", couleur);
        intent.putExtra("NOTE_ID", -1);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadNotes();
        }
    }
}