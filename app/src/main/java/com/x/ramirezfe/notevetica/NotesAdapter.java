package com.x.ramirezfe.notevetica;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Fernando on 3/28/16.
 */

/*
    Adapter Class made for MainActivity's RecyclerView of note objects
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    List<Note> notes;

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.note_title)
        TextView titleTV;
        @Bind(R.id.note_description)
        TextView descriptionTV;
        private View view;
        private Context context;

        public NoteViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
            context = itemView.getContext();
        }

    }

    public NotesAdapter(List<Note> notes) {
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
    public void onBindViewHolder(NoteViewHolder viewHolder, int position) {
        Note note = notes.get(position);
        viewHolder.titleTV.setText(note.getTitle());
        viewHolder.descriptionTV.setText(note.getDescription());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void addItemsToList(Note note) {
        notes.add(note);
        notifyDataSetChanged();
    }

    public void refreshBackendList(List<Note> notes) {
        notes.clear();
        notes.addAll(notes);
        notifyDataSetChanged();
    }

}