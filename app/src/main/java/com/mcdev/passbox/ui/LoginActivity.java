package com.mcdev.passbox.ui;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Loginer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Context mContext;
	private Button loginButton;
    private EditText display;
	private StringBuilder pin = new StringBuilder();
    // Activity static instance used to finish it from another Activity
	public static Activity loginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mContext = this;
		loginActivity = this;
		
		// Check if is the first time the app is launched
		Loginer.getInstance(mContext).checkLogin();
		
		// Find Views
		display = (EditText) findViewById(R.id.display);
		loginButton = (Button) findViewById(R.id.ok);
        ImageButton del = (ImageButton) findViewById(R.id.del);
        Button k1 = (Button) findViewById(R.id.key1);
        Button k2 = (Button) findViewById(R.id.key2);
        Button k3 = (Button) findViewById(R.id.key3);
        Button k4 = (Button) findViewById(R.id.key4);
        Button k5 = (Button) findViewById(R.id.key5);
        Button k6 = (Button) findViewById(R.id.key6);
        Button k7 = (Button) findViewById(R.id.key7);
        Button k8 = (Button) findViewById(R.id.key8);
        Button k9 = (Button) findViewById(R.id.key9);
        Button k0 = (Button) findViewById(R.id.key0);
		
		// Set OnTouchListener to the EditText
		display.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		display.setFocusable(false);
		
		// OnClickListener
		OnClickListener keyboardListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.key1:
					appendChar("1");
					break;
				case R.id.key2:
					appendChar("2");
					break;
				case R.id.key3:
					appendChar("3");
					break;
				case R.id.key4:
					appendChar("4");
					break;
				case R.id.key5:
					appendChar("5");
					break;
				case R.id.key6:
					appendChar("6");
					break;
				case R.id.key7:
					appendChar("7");
					break;
				case R.id.key8:
					appendChar("8");
					break;
				case R.id.key9:
					appendChar("9");
					break;
				case R.id.key0:
					appendChar("0");
					break;
				case R.id.del:
					deleteLastChar();
					break;
				case R.id.ok:
					checkPwd();
					break;

				default:
					break;
				}
			}
		};
		
		// Set the listener to the buttons
		k1.setOnClickListener(keyboardListener);
		k2.setOnClickListener(keyboardListener);
		k3.setOnClickListener(keyboardListener);
		k4.setOnClickListener(keyboardListener);
		k5.setOnClickListener(keyboardListener);
		k6.setOnClickListener(keyboardListener);
		k7.setOnClickListener(keyboardListener);
		k8.setOnClickListener(keyboardListener);
		k9.setOnClickListener(keyboardListener);
		k0.setOnClickListener(keyboardListener);
		del.setOnClickListener(keyboardListener);
		loginButton.setOnClickListener(keyboardListener);
		
	}
	
	/**
	 * Append the char of the pressed button to the PIN
	 * @param keyToAppend the char to append
	 */
	private void appendChar(String keyToAppend) {
		if (pin.length() < 8) {
			pin.append(keyToAppend);
			display.setText(pin.toString());
		}
		toggleOkButton();
	}
	
	/**
	 * Delete the last char of the PIN
	 */
	private void deleteLastChar() {
		if (pin.length() > 0) {
			pin.delete(pin.length() - 1, pin.length());
			display.setText(pin.toString());
		}
		toggleOkButton();
	}
	
	/**
	 * Toggle the visibility of the OK button
	 */
	private void toggleOkButton() {
		if (pin.length() > 3) {
			loginButton.setVisibility(View.VISIBLE);
		} else {
			loginButton.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Verify the entered password
	 */
	private void checkPwd() {
		String storedPwd = Loginer.getInstance(mContext).getMainPwd();
		if (storedPwd.equals(pin.toString())) {
			Intent mIntent = new Intent(mContext, MainActivity.class);
			startActivity(mIntent);
			finish();
		} else {
			Toast.makeText(mContext, getResources().getString(R.string.wrong_password), Toast.LENGTH_SHORT).show();
			pin = new StringBuilder();
			display.setText(pin.toString());
		}
	}
}
