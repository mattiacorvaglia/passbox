package com.mcdev.passbox.views;

/**
 * Custom DialogPreference
 * @author Mattia Corvaglia
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Util;

/**
 * Custom DialogPreference
 * @author Mattia Corvaglia
 */
public class ImportDBPreference extends DialogPreference {

    // Constructor
    public ImportDBPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Perform the import
                 */
                Util.Files.chooseBackup(getContext());
                dialog.dismiss();
            }
        });
    }

}

