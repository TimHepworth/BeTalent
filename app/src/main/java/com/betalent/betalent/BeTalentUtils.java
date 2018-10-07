package com.betalent.betalent;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

public class BeTalentUtils {

    public static void showAlert(Context context, String alertTitle, String alertMessage) {

        // setup the alert builder
        ContextThemeWrapper cwr = new ContextThemeWrapper(context, R.style.AlertDialogCustom);

        AlertDialog.Builder builder = new AlertDialog.Builder(cwr);

        TextView dialogTitle = new TextView(context);
        dialogTitle.setText("   " + alertTitle);
        dialogTitle.setTypeface(null, Typeface.BOLD);
        //dialogTitle.setTextColor(Color.parseColor("#DE4943"));
        dialogTitle.setTextColor(Color.parseColor("#C62263"));
        dialogTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        builder.setCustomTitle(dialogTitle);
        builder.setMessage(alertMessage);

        // add a button
        builder.setPositiveButton(R.string.ok, null);
        //builder.setNegativeButton(R.string.cancel, null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffffff"));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f);
    }

}
