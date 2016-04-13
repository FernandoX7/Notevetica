package com.x.ramirezfe.notevetica;

/**
 * Created by Fernando on 4/7/16.
 */

import com.backendless.Backendless;

/**
 * Sugar ORM
 * -Extends SugarApp so we can use Sugar ORM for offline saving
 */

public class NoteApplication extends com.orm.SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Backendless
        Backendless.initApp(this, Constants.APP_ID, Constants.SECRET_KEY, Constants.VERSION);
    }

}