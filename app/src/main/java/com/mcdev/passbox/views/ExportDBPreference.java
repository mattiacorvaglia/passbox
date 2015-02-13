package com.mcdev.passbox.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                int result = Util.Files.exportDB();

                switch (result) {
                    case 1:
//                        Toast.makeText(getContext(), getContext().getString(R.string.export_done), Toast.LENGTH_LONG).show();
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ok_message));
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, null, (DialogInterface.OnClickListener) null);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.the_end), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case 2:
//                        Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, null, (DialogInterface.OnClickListener) null);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.the_end), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case 3:
//                        Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, null, (DialogInterface.OnClickListener) null);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.the_end), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    default:
//                        Toast.makeText(getContext(), getContext().getString(R.string.export_error), Toast.LENGTH_LONG).show();
                        dialog.setTitle(getContext().getString(R.string.export_ko_title));
                        dialog.setMessage(getContext().getString(R.string.export_ko_message) +
                                getContext().getString(R.string.export_ko_exception_1));
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, null, (DialogInterface.OnClickListener) null);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getContext().getString(R.string.the_end), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }

}
