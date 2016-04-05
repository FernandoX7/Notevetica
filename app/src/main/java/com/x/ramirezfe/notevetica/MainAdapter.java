package com.x.ramirezfe.notevetica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Fernando on 3/28/16.
 */

/*
    Adapter Class made for MainActivity's RecyclerView
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NoteViewHolder> {

    List<Note> notes;

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView noteTitle;
        TextView noteDescription;
        private View view;
        private TextView titleTV;
        private TextView descriptionTV;
        private Context context;

        NoteViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            context = itemView.getContext();
            titleTV = (TextView) view.findViewById(R.id.note_title);
            descriptionTV = (TextView) view.findViewById(R.id.note_description);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.notes_card_view);
            noteTitle = (TextView) itemView.findViewById(R.id.note_title);
            noteDescription = (TextView) itemView.findViewById(R.id.note_description);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(v.getContext(), "Position is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

        }

        @Override
        public void onClick(View view) {
            CreateNoteActivity.didClick = true;
            CreateNoteActivity.EXTRA_ID = getAdapterPosition();
            Log.d("TAG", "onClick() called on row: " + getAdapterPosition());
            Intent intent = new Intent(context, CreateNoteActivity.class);
            intent.putExtra(CreateNoteActivity.EXTRA_TITLE, titleTV.getText().toString());
            intent.putExtra(CreateNoteActivity.EXTRA_DESCRIPTION, descriptionTV.getText().toString());
            intent.putExtra("ID", getAdapterPosition());
            ((Activity) context).startActivityForResult(intent, 123);
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
