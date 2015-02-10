package com.mcdev.passbox.ui;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Loginer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SetLoginActivity extends Activity {
	
	private Context mContext;
	private Button saveButton, key1, key2, key3, key4, key5, key6, key7, key8, key9, key0;
	private ImageButton delButton;
	private TextView repeatText;
	private EditText displayView;
	private StringBuilder newPin = new StringBuilder();
	private String preconfirmed;
	private int steps = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_login);
		
		mContext = this;
		
		// Find Views
		displayView = (EditText) findViewById(R.id.display_view);
		repeatText = (TextView) findViewById(R.id.repeat_text);
		saveButton = (Button) findViewById(R.id.save_pin);
		delButton = (ImageButton) findViewById(R.id.del_btn);
		key1 = (Button) findViewById(R.id.skey1);
		key2 = (Button) findViewById(R.id.skey2);
		key3 = (Button) findViewById(R.id.skey3);
		key4 = (Button) findViewById(R.id.skey4);
		key5 = (Button) findViewById(R.id.skey5);
		key6 = (Button) findViewById(R.id.skey6);
		key7 = (Button) findViewById(R.id.skey7);
		key8 = (Button) findViewById(R.id.skey8);
		key9 = (Button) findViewById(R.id.skey9);
		key0 = (Button) findViewById(R.id.skey0);
		
		// Set OnTouchListener to the EditText
		displayView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		displayView.setFocusable(false);
		
		// OnClickListener
		OnClickListener keyboardListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.skey1:
					appendChar("1");
					break;
				case R.id.skey2:
					appendChar("2");
					break;
				case R.id.skey3:
					appendChar("3");
					break;
				case R.id.skey4:
					appendChar("4");
					break;
				case R.id.skey5:
					appendChar("5");
					break;
				case R.id.skey6:
					appendChar("6");
					break;
				case R.id.skey7:
					appendChar("7");
					break;
				case R.id.skey8:
					appendChar("8");
					break;
				case R.id.skey9:
					appendChar("9");
					break;
				case R.id.skey0:
					appendChar("0");
					break;
				case R.id.del_btn:
					deleteLastChar();
					break;
				case R.id.save_pin:
					steps++;
					checkSteps();
					break;

				default:
					break;
				}
			}
		};
		
		// Set the listener to the buttons
		key1.setOnClickListener(keyboardListener);
		key2.setOnClickListener(keyboardListener);
		key3.setOnClickListener(keyboardListener);
		key4.setOnClickListener(keyboardListener);
		key5.setOnClickListener(keyboardListener);
		key6.setOnClickListener(keyboardListener);
		key7.setOnClickListener(keyboardListener);
		key8.setOnClickListener(keyboardListener);
		key9.setOnClickListener(keyboardListener);
		key0.setOnClickListener(keyboardListener);
		delButton.setOnClickListener(keyboardListener);
		saveButton.setOnClickListener(keyboardListener);
		
		// Show the AlertDialog
		new AlertDialog.Builder(this)
		.setTitle(getResources().getString(R.string.welcome))
		.setMessage(getResources().getString(R.string.set_auth_code))
		.setCancelable(false)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {}
		})
//		.setIcon(android.R.drawable.ic_dialog_info)
		.show();

	}

	/**
	 * Append the char of the pressed button to the PIN
	 * @param keyToAppend
	 */
	private void appendChar(String keyToAppend) {
		if (newPin.length() < 8) {
			newPin.append(keyToAppend);
			displayView.setText(newPin.toString());
		}
		toggleOkButton();
	}
	
	/**
	 * Delete the last char of the PIN
	 */
	private void deleteLastChar() {
		if (newPin.length() > 0) {
			newPin.delete(newPin.length() - 1, newPin.length());
			displayView.setText(newPin.toString());
		}
		toggleOkButton();
	}
	
	/**
	 * Toggle the visibility of the OK button
	 */
	private void toggleOkButton() {
		if (newPin.length() > 3) {
			saveButton.setVisibility(View.VISIBLE);
		} else {
			saveButton.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Reset the StringBuilder
	 */
	private void resetPin() {
		newPin = new StringBuilder();
		displayView.setText(newPin.toString());
	}
	
	/**
	 * Handle confirmation
	 */
	private void checkSteps() {
		if (steps  == 1) {
			preconfirmed = "";
			preconfirmed = newPin.toString();
			resetPin();
			repeatText.setText(getResources().getString(R.string.repeat_password));
			saveButton.setText(getResources().getString(R.string.confirm));
			saveButton.setVisibility(View.INVISIBLE);
		} else if (steps  == 2) {
			if (newPin.toString().equals(preconfirmed)) {
				Loginer.getInstance(mContext).setLogin(newPin.toString());
				resetPin();
				steps = 0;
				repeatText.setText(getResources().getString(R.string.ok_password));
				saveButton.setVisibility(View.INVISIBLE);
				Intent mIntent = new Intent(mContext, MainActivity.class);
				startActivity(mIntent);
				finish();
			} else {
				repeatText.setText(getResources().getString(R.string.repeat_process));
				saveButton.setText(getResources().getString(R.string.save));
				saveButton.setVisibility(View.INVISIBLE);
				resetPin();
				steps = 0;
			}
		} else {
			// Error
			resetPin();
			steps = 0;
		}
	}
	
}
