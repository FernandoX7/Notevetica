package com.x.ramirezfe.notevetica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/*
    Activity used to create a new note
    Comes from MainActivity
 */

public class CreateNoteActivity extends AppCompatActivity {

    // Views
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_description)
    EditText etDescription;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    // Misc. Variables
    private String etTitleText, etDescriptionText;
    private static String TAG = CreateNoteActivity.class.getSimpleName();
    // Objects
    private Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        // ButterKnife
        ButterKnife.bind(this);
        // Toolbar
        setSupportActionBar(toolbar);
        // Open keyboard automatically
        showKeyboard();

        // Check if the note extras were passed (did the user click a note or not)
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String passedTitle = intent.getStringExtra(MainActivity.EXTRA_NOTE_TITLE);
            String passedDescription = intent.getStringExtra(MainActivity.EXTRA_NOTE_DESCRIPTION);
            String passedUUID = intent.getStringExtra(MainActivity.EXTRA_NOTE_UUID);
            note.setTitle(passedTitle);
            note.setDescription(passedDescription);
            note.setObjectId(passedUUID);
            etTitle.setText(passedTitle);
            etDescription.setText(passedDescription);
        }

        // Save Note FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareForSavingNote(view);
            }

        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        // Check if the user has unsaved changes
        prepareForSavingNote(null);
    }

    /*
        This method checks to see if a valid note can be created
        @param view - Your view. (It CAN be null if you don't need the snackbar, which requires a view)
     */
    public void prepareForSavingNote(View view) {
        // Get the EditText's latest values
        refreshFieldData();
        // Remove whitespaces at the beginning/end
        etTitleText = etTitleText.trim();
        etDescriptionText = etDescriptionText.trim();
        // Retrieve the notes data passed by MainActivity's intent
        Intent intent = getIntent();
        String passedTitle = intent.getStringExtra(MainActivity.EXTRA_NOTE_TITLE);
        String passedDescription = intent.getStringExtra(MainActivity.EXTRA_NOTE_DESCRIPTION);

        // If note title is empty, DO NOT save
        if (etTitleText.equals("") || etTitleText.isEmpty()) {
            // If the user clicks the back button with nothing filled out, finish();
            if (view == null) {
                finish();
            } else {
                Notify.snack(view, "Title may not be empty");
            }
            // User clicked a note they've already created and did no changes to it
        } else if (etTitleText.equals(passedTitle) && etDescriptionText.equals(passedDescription)) {
            finish();
            // The user clicked an existing note and pressed the back button
            // If the title or description is different then resave the note
        } else if (!etTitleText.equals(passedTitle) || !etDescriptionText.equals(passedDescription)) {
            if (view == null) {
                // User is leaving by pressing the back button and has unsaved changes...alert them
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Would you like to save your changes?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveNote(); // Resave note
                        finish();
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
    }

    // Called after "prepareForSavingNote()" declares that a note meets the criteria to be saved
    public void saveNote() {
        // Get the EditText's latest values
        refreshFieldData();
        // Apply the data to the note object
        note.setTitle(etTitleText);
        note.setDescription(etDescriptionText);

        // Save to the backend
        Backendless.Persistence.save(note, new AsyncCallback<Note>() {
            public void handleResponse(Note response) {

                Notify.out("*ONLINE* Successfully saved the following note: " + response.toString());
                /**
                 * Sugar ORM
                 * -Save/resave offline note
                 */
                Intent intent = getIntent();
                if (intent.getExtras() != null) {
                    String passedUUID = intent.getStringExtra(MainActivity.EXTRA_NOTE_UUID);
                    // Resave the offline note
                    if (passedUUID.equals(response.getObjectId())) {
                        Select getExistingNote = Select.from(Note.class).where(Condition.prop("object_id").eq(passedUUID));
                        List<Note> existingNotes = getExistingNote.list();
                        for (Note note : existingNotes) {
                            note.setTitle(response.getTitle());
                            note.setDescription(response.getDescription());
                            note.setUpdated(response.getUpdated());
                            note.save();
                            finish();
                            Notify.out("*OFFLINE* Successfully resaved the following note: " + note.toString());
                        }
                    }
                } else {
                    // Save a new offline note
                    note.setCreated(response.getCreated());
                    note.save();
                    finish();
                    Notify.out("*OFFLINE* Successfully saved the following note: " + note.toString());
                }
            }

            public void handleFault(BackendlessFault fault) {
                // An error has occurred
                Log.e(TAG, fault.getCode());
            }
        });
    }

    public void onPause() {
        super.onPause();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show the menu if a user clicked on the note, not just made a new one
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Boolean passedContextMenuVisibility = intent.getBooleanExtra(MainActivity.EXTRA_SHOW_CONTEXT_MENU, false);
            if (passedContextMenuVisibility) {
                getMenuInflater().inflate(R.menu.menu_create_note_activity, menu);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Check if the user has unsaved changes
            prepareForSavingNote(null);
            return true;
        }

        if (id == R.id.action_delete) {
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                String passedTitle = intent.getStringExtra(MainActivity.EXTRA_NOTE_TITLE);
                String passedDescription = intent.getStringExtra(MainActivity.EXTRA_NOTE_DESCRIPTION);
                String passedUUID = intent.getStringExtra(MainActivity.EXTRA_NOTE_UUID);
                Note note = new Note();
                note.setTitle(passedTitle);
                note.setDescription(passedDescription);
                note.setObjectId(passedUUID);
            }
            // Alert the user that they are about to delete a note
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Are you sure you want to delete the following note?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Backendless.Persistence.of(Note.class).remove(note, new AsyncCallback<Long>() {
                        public void handleResponse(Long response) {
                            // Note has been deleted. The response is a time in milliseconds when the object was deleted
                            /**
                             * Sugar ORM
                             * -Delete offline note
                             */
                            Intent intent = getIntent();
                            if (intent.getExtras() != null) {
                                String passedUUID = intent.getStringExtra(MainActivity.EXTRA_NOTE_UUID);
                                if (passedUUID.equals(note.getObjectId())) {
                                    Select getExistingNote = Select.from(Note.class).where(Condition.prop("object_id").eq(passedUUID));
                                    List<Note> existingNotes = getExistingNote.list();
                                    for (Note note : existingNotes) {
                                        note.delete();
                                        finish();
                                        Notify.out("*OFFLINE* Successfully deleted the following note: " + note.toString());
                                    }
                                }
                            }
                            Notify.message(getApplicationContext(), "Note successfully deleted");
                            finish();
                        }

                        public void handleFault(BackendlessFault fault) {
                            Notify.out("Error deleting note, " + fault.getCode() + " " + fault.getMessage());
                        }
                    });
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

            return true;
        }

        if (id == R.id.action_share) {
            Notify.message(getApplicationContext(), "Coming soon");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void refreshFieldData() {
        etTitleText = etTitle.getText().toString();
        etDescriptionText = etDescription.getText().toString();
    }


}