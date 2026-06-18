package com.example.gestionnairedesnotes.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionnairedesnotes.R;
import com.example.gestionnairedesnotes.model.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> listeNotes;
    private final Context context;
    private final OnNoteClickListener listener;

    public interface OnNoteClickListener {
        void onSimpleClick(Note note);
        void onDoubleClick(Note note);
    }

    public NotesAdapter(Context context, List<Note> listeNotes, OnNoteClickListener listener) {
        this.context = context;
        this.listeNotes = listeNotes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = listeNotes.get(position);

        holder.textTitre.setText(note.getTitre());
        holder.textDate.setText(note.getDate());
        holder.layoutNote.setBackgroundColor(Color.parseColor(note.getCouleur()));

        // Afficher l'étoile si favori
        holder.iconFavori.setVisibility(note.isFavori() ? View.VISIBLE : View.GONE);

        // Simple clic
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSimpleClick(note);
        });

        // Double clic
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onDoubleClick(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listeNotes.size();
    }

    public void mettreAJourListe(List<Note> nouvelleListe) {
        this.listeNotes = nouvelleListe;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView textTitre, textDate;
        ImageView iconFavori;
        RelativeLayout layoutNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitre = itemView.findViewById(R.id.textTitre);
            textDate = itemView.findViewById(R.id.textDate);
            iconFavori = itemView.findViewById(R.id.iconFavori);
            layoutNote = itemView.findViewById(R.id.layoutNote);
        }
    }
}