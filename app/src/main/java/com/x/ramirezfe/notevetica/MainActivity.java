package com.x.ramirezfe.notevetica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
    Starting activity for app
    Displays notes in a recycler view
 */

public class MainActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<>();
    private RecyclerView recyclerView;
    MainAdapter adapter;
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide toolbar title

        // RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true); // If the view won't be changing, set to true
        recyclerView.setLayoutManager(linearLayoutManager);

        loadTestData();
        initializeAdapter();

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote();
                // addTestData();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            refreshAdapter();
            Notify.message(getApplicationContext(), "Refreshing");
//            // clearAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadTestData() {
        notes.add(new Note("This is the first notes title", getResources().getString(R.string.lorem_ipsum_short)));
        notes.add(new Note("Anniversary", "Let's think about what to do this year."));
        notes.add(new Note("3 times more video", "We’ve optimized video streaming for all T-Mobile customers, so you can now watch up to 3X more video, stretching your high-speed data farther. Included on all Simple Choice Plans."));
        notes.add(new Note("Medium Sized", getResources().getString(R.string.lorem_ipsum_medium)));
        notes.add(new Note("Thanksgiving Dinner RSVPs", "Eric Jones. Bryan Smith. Lauren Far. Jovanni Aikens."));
        notes.add(new Note("Gift Ideas", "Bike, candles, coffe mug."));
        notes.add(new Note("Reminder", "Remember to take out the trash"));
        notes.add(new Note("Long Size", getResources().getString(R.string.lorem_ipsum_long)));
        notes.add(new Note("Book Ideas", "Robert Jordans meets Ian McEwan meets Donna Tartt"));
        notes.add(new Note("Shopping List", "Brie, bread, sparkling cider, strawberries, chocolate mousse, figs."));
    }

    private void initializeAdapter() {
        adapter = new MainAdapter(notes);
        recyclerView.setAdapter(adapter);
    }

    private void addTestData() {
        Log.d(TAG, "Adding test note");
        notes.add(new Note("Note Title", "Note Description"));
        refreshAdapter();
    }

    private void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                String passedTitle = data.getStringExtra(CreateNoteActivity.EXTRA_TITLE);
                String passedDescription = data.getStringExtra(CreateNoteActivity.EXTRA_DESCRIPTION);
                int passedID = data.getIntExtra("ID", CreateNoteActivity.EXTRA_ID);
                Note note = new Note();
                // TODO: Move last edited note to the top of the list
                if (CreateNoteActivity.didClick) { // User is saving an existing note
                    note.setTitle(passedTitle);
                    note.setDescription(passedDescription);
                    notes.remove(passedID); // Remove note so I can put that same one at the top
                    notes.add(0, note); // Put note at top of list
                    CreateNoteActivity.didClick = false;
                    recyclerView.scrollToPosition(0); // Scroll to top
                } else { // User is creating a new note
                    note.setTitle(passedTitle);
                    note.setDescription(passedDescription);
                    notes.add(0, note);
                    recyclerView.scrollToPosition(0);
                }
            }

            refreshAdapter();

            if (resultCode == Activity.RESULT_CANCELED) {
                // Do something if it's cancelled. Happens when you click the back button for example
            }
        }
    }

    // Called when user clicks the FAB
    public void createNote() {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivityForResult(intent, 123);
    }

    public void clearAllNotes() {
        for (Iterator<Note> iter = notes.listIterator(); iter.hasNext(); ) {
            Note note = iter.next();
            iter.remove();
        }
    }

}
