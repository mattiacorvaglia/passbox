package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PasswordDao;

/**
 * Custom DialogPreference
 * @author Mattia Corvaglia
 */
public class RestoreDBPreference extends DialogPreference {

    // Constructor
    public RestoreDBPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        final AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordDao.getInstance(getContext().getApplicationContext()).open();
                PasswordDao.getInstance(getContext().getApplicationContext()).deleteAllPasswordsAndRecoveries();
                PasswordDao.getInstance(getContext().getApplicationContext()).close();

                dialog.setTitle(getContext().getString(R.string.restore_ok_title));
                dialog.setMessage(getContext().getString(R.string.restore_ok_message));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setText(getContext().getString(R.string.the_end));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

}