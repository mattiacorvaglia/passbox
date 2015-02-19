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
import com.mcdev.passbox.utils.Util;

import static android.view.View.OnClickListener;

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
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = Util.Files.exportDB();

                switch (result) {
                    case Util.Files.EXPORT_COMPLETED:
                        dialog.setTitle(getContext().getString(R.string.export_ok_title));
                        dialog.setMessage(getContext().getString(R.string.export_ok_message));
                        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setVisibility(View.GONE);
                        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setText(getContext().getString(R.string.the_end));
                        positiveButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case Util.Files.EXPORT_CANNOT_WRITE_SD:
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getContext().getString(R.string.retry));
                        break;
                    case Util.Files.EXPORT_IO_EXCEPTION:
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getContext().getString(R.string.retry));
                        break;
                    case Util.Files.EXPORT_EXCEPTION:
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getContext().getString(R.string.retry));
                        break;
                    default:
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(getContext().getString(R.string.retry));
                        break;
                }
            }
        });
    }

}
