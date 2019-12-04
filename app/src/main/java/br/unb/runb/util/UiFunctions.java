package br.unb.runb.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import br.unb.runb.screens.ContainerActivity;
import br.unb.runb.screens.credito.CreditoActivity;

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

    public static void tokenExpired(Context context) {
        //TODO: testar
        Intent intent = new Intent(context, ContainerActivity.class); //TODO: get intent at container and set to login tab
        intent.putExtra("token_expired", true);
        context.startActivity(intent);
        //dialog

    }

}
