package com.x.ramirezfe.notevetica;

/**
 * Created by Fernando on 4/7/16.
 */

import android.app.Application;

import com.backendless.Backendless;

public class NoteApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.SECRET_KEY, Constants.VERSION);
    }

}