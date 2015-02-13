package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Util;

/**
 * Custom DialogPreference
 * @author Mattia Corvaglia
 */
public class ExportDBPreference extends DialogPreference {
    
    // Constructor
    public ExportDBPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
                dialog.dismiss();
            }
        });
    }

    /**
     * Export the database
     * and show the result in a Toast
     */
    private void exportDB() {
        
        int result = Util.Files.exportDB();
        
        switch (result) {
            case 1:
                Toast.makeText(getContext(), getContext().getString(R.string.export_done), Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
        }

    }
}
