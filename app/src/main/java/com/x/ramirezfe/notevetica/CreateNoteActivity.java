package com.x.ramirezfe.notevetica;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/*
    Activity used to create a new note
    Comes from MainActivity
 */

public class CreateNoteActivity extends AppCompatActivity {

    // Views
    private EditText etTitle;
    private EditText etDescription;

    // Intent Extras
    public final static String EXTRA_TITLE = "com.x.ramirezfe.notevetica.TITLE";
    public final static String EXTRA_DESCRIPTION = "com.x.ramirezfe.notevetica.DESCRIPTION";
    public static int EXTRA_ID;

    public static boolean didClick = false; // Used to keep track if the user clicked on an existing note

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init. Views
        etTitle = (EditText) findViewById(R.id.et_title);
        etDescription = (EditText) findViewById(R.id.et_description);

        showKeyboard();

        if (didClick) {
            preparingForNoteEditing();
        }

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareForSavingNote(view);
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    // Called when the save note FAB is clicked
    public void saveNote() {
        Intent intent = new Intent(this, MainActivity.class);
        /* Save Note */
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        // Pass the EditText fields
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        // Go back to MainActivity
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        prepareForSavingNote(null);
    }

    // Check to see if note can be saved
    /*
        @param view - Your view. (It CAN be null if you don't need the snackbar, which requires a view)
     */
    public void prepareForSavingNote(View view) {
        // Check to see if at least one field is populated with data
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        title = title.trim(); // Remove whitespaces at the beginning/end
        description = description.trim();

        // Get intent extras
        Intent intent = getIntent();
        String alreadyCreatedTitle = intent.getStringExtra(CreateNoteActivity.EXTRA_TITLE);
        String alreadyCreatedDescription = intent.getStringExtra(CreateNoteActivity.EXTRA_DESCRIPTION);

        // Check to see if note title is empty, if it is, don't save
        if (title == "" || title.isEmpty()) {
            // Checks to see if the user clicked on create note, then just clicked the back button
            if (view == null) {
                finish();
            } else {
                Notify.snack(view, "Title may not be empty");
            }
            // If the user clicked an already made note and did not change its contents, go back to MainActivity
        } else if (title.equals(alreadyCreatedTitle) && description.equals(alreadyCreatedDescription)) {
            finish();
            CreateNoteActivity.didClick = false;
            // The user is editing a note
        } else if (didClick) {
            // If the title or description is different then resave the note
            if (!title.equals(alreadyCreatedTitle) || !description.equals(alreadyCreatedDescription)) {
                if (view == null) {
                    // User is leaving by pressing the back button and has unsaved changes...alert them
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Would you like to save your changes?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveNote();
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    saveNote();
                }
            }
        } else {
            saveNote();
            CreateNoteActivity.didClick = false;
        }
    }

    // A user has clicked a note
    public void preparingForNoteEditing() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(CreateNoteActivity.EXTRA_TITLE);
        String description = intent.getStringExtra(CreateNoteActivity.EXTRA_DESCRIPTION);
        etTitle.setText(title);
        etDescription.setText(description);
    }

    public void onPause()
    {
        super.onPause();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}

