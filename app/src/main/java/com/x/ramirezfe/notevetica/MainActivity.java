package com.x.ramirezfe.notevetica;

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

import java.util.ArrayList;
import java.util.List;

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

        initializeData();
        initializeAdapter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNote(view);
                // addTestData();
            }
        });

        // Check for notes that may have just been added
        Intent intent = getIntent();
        String passedTitle = intent.getStringExtra(CreateNoteActivity.EXTRA_TITLE);
        String passedDescription = intent.getStringExtra(CreateNoteActivity.EXTRA_DESCRIPTION);
        if (passedTitle != null && passedDescription != null) {
            notes.add(new Note(passedTitle, passedDescription));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeData() {
        notes.add(new Note("This is the first notes title", getResources().getString(R.string.lorem_ipsum_short)));
        notes.add(new Note("Anniversary", "Let's think about what to do this year."));
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

    // Called when user clicks FAB
    public void createNote(View view) {
        Intent intent = new Intent(this, CreateNoteActivity.class);
        startActivity(intent);
    }

}
