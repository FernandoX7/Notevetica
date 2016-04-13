package com.x.ramirezfe.notevetica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    // Views
    @Bind(R.id.signupButton)
    Button signUpButton;
    @Bind(R.id.emailField)
    EditText emailEditText;
    @Bind(R.id.passwordField)
    EditText passwordEditText;
    @Bind(R.id.firstNameField)
    EditText firstNameEditText;
    @Bind(R.id.lastNameField)
    EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Butterknife
        ButterKnife.bind(this);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.signupButton)
    public void submit(final View view) {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Trim whitspace
        firstName = firstName.trim();
        lastName = lastName.trim();
        email = email.trim();
        password = password.trim();

        // Check for fields being empty
        if (firstName.equals("") || firstName.isEmpty()) {
            Notify.snack(view, "First name is required");
        } else if (lastName.equals("") || lastName.isEmpty()) {
            Notify.snack(view, "Last name is required");
        } else if (email.equals("") || email.isEmpty()) {
            Notify.snack(view, "Email is required");
        } else if (password.equals("") || password.isEmpty()) {
            Notify.snack(view, "Password is required");
        } else {
            // Check if password is a minimum length
            if (password.length() < 5) {
                Notify.snack(view, "Password must be at least 5 characters long");
            } else {
                BackendlessUser user = new BackendlessUser();
                user.setEmail(email);
                user.setProperty("firstName", firstName);
                user.setProperty("lastName", lastName);
                user.setPassword(password);

                Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                    public void handleResponse(BackendlessUser registeredUser) {
                        Notify.out("New User Registered: " + registeredUser.getProperties());
                        Notify.message(getApplicationContext(), "Registration Successful");
                        // Start app
                        successfulLogin();
                    }

                    public void handleFault(BackendlessFault fault) {
                        // Invalid email format
                        if (fault.getCode().equals("3040")) {
                            Notify.snack(view, "Invalid email format");
                            // Email is already taken
                        } else if (fault.getCode().equals("3033")) {
                            Notify.snack(view, "This email is already linked to another account");
                        }
                        Notify.out("Error: " + fault.getCode() + " " + fault.getMessage());
                    }
                });
            }
        }
    }

    // Registration was successful, go to LoginActivity
    private void successfulLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
