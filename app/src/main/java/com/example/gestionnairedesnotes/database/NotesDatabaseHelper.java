package com.example.gestionnairedesnotes.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestionnairedesnotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String NOM_BASE = "notes.db";
    private static final int VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COL_ID = "id";
    public static final String COL_TITRE = "titre";
    public static final String COL_CONTENU = "contenu";
    public static final String COL_COULEUR = "couleur";
    public static final String COL_FAVORI = "favori";
    public static final String COL_DATE = "date";

    public NotesDatabaseHelper(Context context) {
        super(context, NOM_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String requeteCreation = "CREATE TABLE " + TABLE_NOTES + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITRE + " TEXT NOT NULL, "
                + COL_CONTENU + " TEXT, "
                + COL_COULEUR + " TEXT, "
                + COL_FAVORI + " INTEGER DEFAULT 0, "
                + COL_DATE + " TEXT"
                + ")";
        db.execSQL(requeteCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Récupérer toutes les notes
    public List<Note> obtenirToutesLesNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, COL_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                notes.add(cursorVersNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    // Convertir un cursor en Note
    private Note cursorVersNote(Cursor cursor) {
        return new Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENU)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COULEUR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_FAVORI)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
        );
    }
}