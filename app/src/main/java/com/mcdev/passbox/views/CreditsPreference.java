package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;

import com.mcdev.passbox.R;

/**
 * Credits Dialog
 * @author Mattia Corvaglia
 */
public class CreditsPreference extends DialogPreference {

    // Constructor
    public CreditsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(getContext().getString(R.string.the_end));

    }
}
