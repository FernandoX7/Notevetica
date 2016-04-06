package com.x.ramirezfe.notevetica;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Fernando on 3/30/16.
 */

/**
 * The purpose of this class is to replace Toast...for debugging purposes...
 * Mostly because I'm too lazy to type out the whole Toast method. SO LONG
 * Just call Notify.message(context, message)
 */

public class Notify { // An original class lolllllll

    /*
        @param context = your context
        @param message = what you want to say
     */
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
        @param view = your view
        @param message = what you want to say
     */
    public static void snack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}
