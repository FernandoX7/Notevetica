package com.x.ramirezfe.notevetica;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button loginButton;
    protected TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init. Views
        signUpTextView = (TextView) findViewById(R.id.signUpText);
        emailEditText = (EditText) findViewById(R.id.emailField);
        passwordEditText = (EditText) findViewById(R.id.passwordField);
        loginButton = (Button) findViewById(R.id.loginButton);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
//
//        signUpTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString();
//                String password = passwordEditText.getText().toString();
//
//                email = email.trim();
//                password = password.trim();
//
//                if (email.isEmpty() || password.isEmpty()) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                    builder.setMessage(R.string.login_error_message)
//                            .setTitle(R.string.login_error_title)
//                            .setPositiveButton(android.R.string.ok, null);
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
//                    final String emailAddress = email;
//
//                    //Login with an email/password combination
//                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
//                        @Override
//                        public void onAuthenticated(AuthData authData) {
//                            // Authenticated successfully with payload authData
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map.put("email", emailAddress);
//                            ref.child("users").child(authData.getUid()).setValue(map);
//
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onAuthenticationError(FirebaseError firebaseError) {
//                            // Authenticated failed with error firebaseError
//                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                            builder.setMessage(firebaseError.getMessage())
//                                    .setTitle(R.string.login_error_title)
//                                    .setPositiveButton(android.R.string.ok, null);
//                            AlertDialog dialog = builder.create();
//                            dialog.show();
//                        }
//                    });
//                }
//            }
//        });

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
