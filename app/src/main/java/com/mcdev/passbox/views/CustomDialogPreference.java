package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Custom DialogPreference
 * @author Mattia Corvaglia
 */
public class CustomDialogPreference extends DialogPreference {
    
    // Constructor
    public CustomDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exportDB()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * Export the database
     * @return The status of the operation
     */
    private boolean exportDB() {

        Toast.makeText(getContext(), "Prova prova prova", Toast.LENGTH_SHORT).show();
        return true;
    }
}
