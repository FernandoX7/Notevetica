package com.x.ramirezfe.notevetica;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Fernando on 3/30/16.
 */

/**
 * This class exists for just one purpose...lazy debugging.
 * That way we don't have to use Log.d, Toast.make(blahblahblah), or snack.yaKnow
 */

public class Notify { // An original class lolllllll

    /**
     * Creates a Toast
     *
     * @param context your context
     * @param message what you want to say
     */
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates a SnackBar
     *
     * @param view    your view
     * @param message what you want to say
     */
    public static void snack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     * Creates an OG System.out
     *
     * @param message what you want to say
     */
    public static void out(String message) {
        System.out.println("[*]Notify[*] " + message);
    }

    /**
     * Creates an AlertDialog
     *
     * @param context where it occurs (If in activity, use Activity.this)
     * @param title   title of the dialog
     * @param message dialogs message
     * @return dialog containing error message
     */
    public static void alert(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
