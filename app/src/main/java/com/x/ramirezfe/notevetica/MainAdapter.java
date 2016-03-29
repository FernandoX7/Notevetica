package com.x.ramirezfe.notevetica;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fernando on 3/28/16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NoteViewHolder> {

    List<Note> notes;

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView noteTitle;
        TextView noteDescription;

        NoteViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.notes_card_view);
            noteTitle = (TextView) itemView.findViewById(R.id.note_title);
            noteDescription = (TextView) itemView.findViewById(R.id.note_description);
        }
    }

    MainAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder personViewHolder, int i) {
        personViewHolder.noteTitle.setText(notes.get(i).getTitle());
        personViewHolder.noteDescription.setText(notes.get(i).getDescription());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
