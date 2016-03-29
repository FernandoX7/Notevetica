package com.x.ramirezfe.notevetica;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CreateNoteActivity extends AppCompatActivity {

    // Views
    private EditText etTitle;
    private EditText etDescription;

    // Intent Extras
    public final static String EXTRA_TITLE = "com.x.ramirezfe.notevetica.TITLE";
    public final static String EXTRA_DESCRIPTION = "com.x.ramirezfe.notevetica.DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init. Views
        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check to see if at least one field is populated with data
                String title = etTitle.getText().toString();
                title = title.trim(); // Remove whitespaces at the beginning/end
                if (title == "" || title.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Title may not be empty", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    saveNote(view);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Called when the FAB is clicked
    public void saveNote(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        /* Save Note */
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        // Pass the EditText fields
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        // Go back to MainActivity
        startActivity(intent);
        finish();
    }

}

