package com.example.gestionnairedesnotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestionnairedesnotes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COL_ID = "id";
    private static final String COL_TITRE = "titre";
    private static final String COL_CONTENU = "contenu";
    private static final String COL_COULEUR = "couleur";
    private static final String COL_IS_FAVORI = "is_favori";
    private static final String COL_DATE = "date";

    public NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITRE + " TEXT NOT NULL, " +
                COL_CONTENU + " TEXT NOT NULL, " +
                COL_COULEUR + " TEXT NOT NULL, " +
                COL_IS_FAVORI + " INTEGER DEFAULT 0, " +
                COL_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITRE, note.getTitre());
        values.put(COL_CONTENU, note.getContenu());
        values.put(COL_COULEUR, note.getCouleur());
        values.put(COL_IS_FAVORI, note.isFavori() ? 1 : 0);
        values.put(COL_DATE, note.getDate());
        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null,
                COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        Note note = null;
        if (cursor.moveToFirst()) {
            note = cursorToNote(cursor);
        }
        cursor.close();
        db.close();
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, COL_DATE + " DESC");
        if (cursor.moveToFirst()) {
            do {
                notes.add(cursorToNote(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITRE, note.getTitre());
        values.put(COL_CONTENU, note.getContenu());
        values.put(COL_COULEUR, note.getCouleur());
        values.put(COL_IS_FAVORI, note.isFavori() ? 1 : 0);
        values.put(COL_DATE, note.getDate());
        int rows = db.update(TABLE_NOTES, values, COL_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rows;
    }

    private Note cursorToNote(Cursor cursor) {
        return new Note(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTENU)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_COULEUR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_FAVORI)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
        );
    }
}
