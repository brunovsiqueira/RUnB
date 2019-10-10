package br.unb.runb.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class UiFunctions {

    public static Dialog showDilalog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
