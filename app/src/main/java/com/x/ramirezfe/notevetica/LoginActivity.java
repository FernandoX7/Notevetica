package com.x.ramirezfe.notevetica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LoginActivity is used to...
 * -Log in the user
 * -Take the user to SignUpActivity if not already registered
 * -Verify that the user is logged in, if they are, it takes them to MainActivity
 */

public class LoginActivity extends AppCompatActivity {

    // Views
    @Bind(R.id.signUpText)
    TextView signUpTextView;
    @Bind(R.id.emailField)
    EditText emailEditText;
    @Bind(R.id.passwordField)
    EditText passwordEditText;
    @Bind(R.id.loginButton)
    Button loginButton;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    // Shared Preferences
    public final static String SKIP_TO_MAIN_ACTIVITY = "com.x.ramirezfe.notevetica.SKIP_TO_MAIN_ACTIVITY";
    public final static String SUCCESSFULLY_LOGGED_IN = "com.x.ramirezfe.notevetica.BOOLEAN_LOGGED_IN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if there's a user logged in already through SharedPreferences
        userIsLoggedIn();

        setContentView(R.layout.activity_login);

        // Butterknife
        ButterKnife.bind(this);

        // Toolbar
        setSupportActionBar(toolbar);

    }

    @OnClick(R.id.signUpText)
    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.loginButton)
    public void login(View view) {
        // Check for an internet connection in a different thread
        new Thread() {
            public void run() {
                try {
                    if (hasInternetAccess(getApplicationContext())) {
                        // Get info
                        String email = emailEditText.getText().toString();
                        String password = passwordEditText.getText().toString();

                        // Check if fields are not empty
                        if (email.equals("") || email.isEmpty()) {
                            //Notify.snack(view, "Email cannot be empty");
                        } else if (password.equals("") || password.isEmpty()) {
                            //  Notify.snack(view, "Password cannot be empty");
                        }

                        // Log in
                        Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                            public void handleResponse(BackendlessUser user) {
                                Notify.out("Login Successful. " + user.getProperties());
                                Backendless.UserService.setCurrentUser(user);
                                /*
                                    Using SharedPreferences to know if a user has logged in.
                                    If they have go, straight to the MainActivity
                                 */
                                Boolean userLoggedIn = true;
                                SharedPreferences.Editor editor = getSharedPreferences(SKIP_TO_MAIN_ACTIVITY, MODE_PRIVATE).edit();
                                editor.putBoolean(SUCCESSFULLY_LOGGED_IN, userLoggedIn);
                                editor.commit();
                                successfulLogin();
                            }

                            public void handleFault(BackendlessFault fault) {
                                // Invalid username/password
                                if (fault.getCode().equals("3003")) {
                                    Notify.message(getApplicationContext(), "Invalid username and/or password");
                                }
                                Notify.out("Error, " + fault.getCode() + " " + fault.getMessage());
                            }
                        }, true);
                        // No internet connection
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Notify.message(getApplicationContext(), "No internet connection detected");
                            }
                        });
                    }
                } catch (Exception e) {
                    Notify.out("Exception: " + e.getMessage());
                }
            }
        }.start();
    }

    // Login was good, start MainActivity
    private void successfulLogin() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * Checks if there is a user logged in
     **/
    private boolean userIsLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(SKIP_TO_MAIN_ACTIVITY, MODE_PRIVATE);
        Boolean userIsLoggedIn = prefs.getBoolean(SUCCESSFULLY_LOGGED_IN, false);
        if (userIsLoggedIn) {
            successfulLogin();
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if a network connection is detected
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Detect internet connection by calling an https request googles network portal
     *
     * @param context your context
     * @return true if a network connection is detected
     */
    public boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
            } catch (IOException e) {
                Notify.out("Error checking internet connection" + e.getMessage());
            }
        } else {
            Notify.out("No network available");
        }
        return false;
    }

}
