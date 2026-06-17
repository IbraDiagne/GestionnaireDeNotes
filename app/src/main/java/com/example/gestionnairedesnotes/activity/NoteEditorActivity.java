package com.example.gestionnairedesnotes.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestionnairedesnotes.R;
import com.example.gestionnairedesnotes.database.NotesDatabaseHelper;
import com.example.gestionnairedesnotes.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {

    public static final String EXTRA_COLOR = "NOTE_COLOR";
    public static final String EXTRA_NOTE_ID = "NOTE_ID";

    private EditText editTitle, editContent;
    private Button btnSave;
    private RelativeLayout editorContainer;
    private NotesDatabaseHelper dbHelper;

    private String couleurChoisie = "#219653"; // Vert par défaut si rien n'est reçu
    private int noteId = -1;
    private boolean isNoteFavori = false; // Sauvegarde le statut favori d'origine en mode modification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);
        editorContainer = findViewById(R.id.editorContainer);
        dbHelper = new NotesDatabaseHelper(this);

        // Récupérer les extras de l'Intent passés par la MainActivity
        if (getIntent().hasExtra(EXTRA_COLOR)) {
            couleurChoisie = getIntent().getStringExtra(EXTRA_COLOR);
        }
        noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

        // Appliquer la couleur de fond dynamique reçue
        if (couleurChoisie != null) {
            editorContainer.setBackgroundColor(Color.parseColor(couleurChoisie));
        }

        // MODE MODIFICATION : On charge la note existante pour pré-remplir les champs
        if (noteId != -1) {
            btnSave.setText("Modifier"); // Changement du texte du bouton selon la maquette

            Note note = dbHelper.getNoteById(noteId);
            if (note != null) {
                editTitle.setText(note.getTitre());
                editContent.setText(note.getContenu());
                couleurChoisie = note.getCouleur();
                isNoteFavori = note.isFavori(); // On stocke l'état favori pour ne pas l'écraser

                // Si la note avait une autre couleur en BDD, on met à jour le fond
                if (couleurChoisie != null) {
                    editorContainer.setBackgroundColor(Color.parseColor(couleurChoisie));
                }
            }
        }

        btnSave.setOnClickListener(v -> sauvegarderNote());
    }

    private void sauvegarderNote() {
        String titre = editTitle.getText().toString().trim();
        String contenu = editContent.getText().toString().trim();

        // Règle de validation : titre et contenu obligatoires
        if (titre.isEmpty() || contenu.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format de date attendu par le sujet (Ex: 17 Juin 2026)
        String date = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE).format(new Date());

        if (noteId == -1) {
            // Mode création : nouvelle note (isFavori est false par défaut)
            Note note = new Note(titre, contenu, couleurChoisie, false, date);
            dbHelper.insertNote(note);
            Toast.makeText(this, "Note créée !", Toast.LENGTH_SHORT).show();
        } else {
            // Mode modification : on met à jour en conservant l'id et le statut favori d'origine
            Note note = new Note(noteId, titre, contenu, couleurChoisie, isNoteFavori, date);
            dbHelper.updateNote(note);
            Toast.makeText(this, "Note modifiée !", Toast.LENGTH_SHORT).show();
        }

        // Notifier la MainActivity que la base de données a changé
        setResult(RESULT_OK);
        finish(); // Ferme l'activité et retourne à l'écran précédent
    }
}
