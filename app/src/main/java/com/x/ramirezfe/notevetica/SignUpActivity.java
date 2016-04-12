package com.x.ramirezfe.notevetica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    protected EditText passwordEditText;
    protected EditText emailEditText;
    protected Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Init. Views
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        emailEditText = (EditText) findViewById(R.id.emailField);
        signUpButton = (Button) findViewById(R.id.signupButton);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
//
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String password = passwordEditText.getText().toString();
//                String email = emailEditText.getText().toString();
//
//                password = password.trim();
//                email = email.trim();
//
//                if (password.isEmpty() || email.isEmpty()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//                    builder.setMessage(R.string.signup_error_message)
//                            .setTitle(R.string.signup_error_title)
//                            .setPositiveButton(android.R.string.ok, null);
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
//                    // signup
//                    ref.createUser(email, password, new Firebase.ResultHandler() {
//                        @Override
//                        public void onSuccess() {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//                            builder.setMessage(R.string.signup_success)
//                                    .setPositiveButton(R.string.login_button_label, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            startActivity(intent);
//                                        }
//                                    });
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }
//
//                        @Override
//                        public void onError(FirebaseError firebaseError) {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
//                            builder.setMessage(firebaseError.getMessage())
//                                    .setTitle(R.string.signup_error_title)
//                                    .setPositiveButton(android.R.string.ok, null);
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }
//                    });
//                }
//            }
//        });

    }

}
