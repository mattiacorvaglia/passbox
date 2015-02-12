package com.mcdev.passbox.ui;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedHashMap;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.content.RecoveryDto;
import com.mcdev.passbox.utils.Constants;
import com.mcdev.passbox.utils.Loginer;
import com.mcdev.passbox.views.colorpicker.ColorPickerDialog;
import com.mcdev.passbox.views.colorpicker.ColorPickerSwatch;
import com.mcdev.passbox.utils.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AddPasswordActivity extends ActionBarActivity {
	
	private Context mContext;
	private int mSelectedColor = -5317; // Yellow is the default
	private LinearLayout recoveryContainer;
	private LinkedHashMap<EditText, EditText> recoveryEditTextList;
	private EditText title, username, password, webUrl, description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_password);
		
		mContext = this;
		
		// Toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Enable back button in the Toolbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Inflating
		recoveryContainer = (LinearLayout) findViewById(R.id.pwd_add_recovery_container);
		title 		= (EditText) findViewById(R.id.pwd_add_title);
		username 	= (EditText) findViewById(R.id.pwd_add_username);
		password 	= (EditText) findViewById(R.id.pwd_add_pwd);
		webUrl 		= (EditText) findViewById(R.id.pwd_add_web_url);
		description = (EditText) findViewById(R.id.pwd_add_description);
		
		// Initialize the Map of EditText for the recovery entries
		recoveryEditTextList = new LinkedHashMap<>();
				 
		// Set the listener for the add button
		Button addRecovery = (Button) findViewById(R.id.pwd_add_recovery);
		addRecovery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Add view
				addRecoveryEntry();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_password_add, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				finish();
				return true;
			case R.id.action_edit_password:
				addPassword();
				return true;
			case R.id.action_set_color:
				showColorPicker();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Add a recovery entry layout
	 */
	private void addRecoveryEntry() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mView = inflater.inflate(R.layout.item_recovery, recoveryContainer, false);
		
		// Add the recovery entry text in the Map
		final EditText mQuestionEditText = (EditText) mView.findViewById(R.id.pwd_add_question);
		EditText mAnswerEditText = (EditText) mView.findViewById(R.id.pwd_add_answer);
		recoveryEditTextList.put(mQuestionEditText, mAnswerEditText);
		
		// Set the remove button listener
		Button removeRecovery = (Button) mView.findViewById(R.id.pwd_remove_recovery);
		removeRecovery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recoveryContainer.removeView(mView);
				recoveryEditTextList.remove(mQuestionEditText);
			}
		});
	    
	    // Add the inflated view
		recoveryContainer.addView(mView);
	}
	
	/**
	 * Add the new password in the database
	 */
    private void addPassword() {

        // Initialize a new password
        PasswordDto mPassword = new PasswordDto();

        // Check not null title
        String insertedTitle = title.getText().toString();
        if (Util.Strings.isNullOrEmpty(insertedTitle)) {
            title.setError(getString(R.string.error_add_title));
        } else {
            mPassword.setTitle(insertedTitle);
            // Check not null password
            String insertedPwd = password.getText().toString();
            if (Util.Strings.isNullOrEmpty(insertedPwd)) {
                password.setError(getString(R.string.error_add_pwd));
            } else {
                mPassword.setPassword(insertedPwd);
                // Check not null username
                String insertedUsername = username.getText().toString();
                if (!Util.Strings.isNullOrEmpty(insertedUsername)) {
                    mPassword.setUsername(insertedUsername);
                }
                // Check not null web URL
                String insertedWebUrl = webUrl.getText().toString();
                if (!Util.Strings.isNullOrEmpty(insertedWebUrl)) {
                    mPassword.setWebUrl(insertedWebUrl);
                }
                // Check not null description
                String insertedDescription = description.getText().toString();
                if (!Util.Strings.isNullOrEmpty(insertedDescription)) {
                    mPassword.setDescription(insertedDescription);
                }
                // Check not null color
                String insertedColor = Util.Colors.toString(mSelectedColor);
                Log.w(Constants.TAG_APPLICATION_LOG, "Selected color = " + String.valueOf(mSelectedColor));
                mPassword.setColor(insertedColor);

                // Create the recovery list
                boolean RecoveryDone = false;
                if (recoveryEditTextList == null || recoveryEditTextList.size() < 1) {
                    RecoveryDone = true;
                } else {
                    for (EditText mQuestion : recoveryEditTextList.keySet()) {
                        RecoveryDto mRecovery = new RecoveryDto();
                        String insertedQuestion = mQuestion.getText().toString();
                        // Check not null question
                        if (Util.Strings.isNullOrEmpty(insertedQuestion)) {
                            mQuestion.setError(getResources().getString(R.string.error_add_question));
                            RecoveryDone = false;
                        } else {
                            mRecovery.setQuestion(insertedQuestion);
                            // Check not null answer
                            EditText mAnswer = recoveryEditTextList.get(mQuestion);
                            String insertedAnswer = mAnswer.getText().toString();
                            if (Util.Strings.isNullOrEmpty(insertedAnswer)) {
                                mAnswer.setError(getResources().getString(R.string.error_add_answer));
                                RecoveryDone = false;
                            } else {
                                mRecovery.setAnswer(insertedAnswer);
                                mPassword.addRecovery(mRecovery);
                                RecoveryDone = true;
                            }
                        }
                    }
                }
                if (RecoveryDone) {
                    // Store in the database
                    new StorePassword(mPassword).execute();
                }
            }
        }
    }

    /**
     * Async Task used to encrypt password
     * and store it in the database
     */
    private class StorePassword extends AsyncTask<String, Void, Long> {

        private PasswordDto mPassword;

        // Constructor
        private StorePassword(PasswordDto mPassword) {
            this.mPassword = mPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(String... params) {
            /**
             * Encrypt password before storing it in the database
             */
            try {
                String passphrase = Loginer.getInstance(mContext).getMainPwd();
                String encrypted = Util.Crypto.encrypt(mPassword.getPassword(), passphrase);
                mPassword.setPassword(encrypted);

                // Store in the database
                PasswordDao.getInstance(mContext).open();
                long newId = PasswordDao.getInstance(mContext).insertPassword(mPassword);
                PasswordDao.getInstance(mContext).close();
                
                return newId;
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException |
                    BadPaddingException | InvalidKeyException |
                    InvalidAlgorithmParameterException | NoSuchPaddingException |
                    InvalidKeySpecException | UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.w(Constants.TAG_APPLICATION_LOG, "Error on encrypting the new password");
                Toast.makeText(mContext, getString(R.string.error_encryption), Toast.LENGTH_SHORT).show();
            } else {
                if (result == -1) {
                    Log.w(Constants.TAG_APPLICATION_LOG, "Error on storing the new password in the database");
                    Toast.makeText(mContext, getString(R.string.error_storing), Toast.LENGTH_SHORT).show();
                } else {
                    // Close this screen
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }
	
	/**
	 * Show the color picker fragment dialog
	 */
	private void showColorPicker() {
		// Get the list of default colors to show
		int[] mColorChoices = Util.Colors.colorChoice(this);
		ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(
				R.string.color_picker_default_title, 	// Dialog title
				mColorChoices,							// List of colors choices
				mSelectedColor,							// Actual selected color
				4,										// Columns
				(Util.isTablet(this)) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL);	// Size of the screen

		// Implement listener to get selected color value
		colorcalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
			@Override
			public void onColorSelected(int color) {
				mSelectedColor = color;
			}
		});
		colorcalendar.show(getSupportFragmentManager(), "colorPicker");
	}
	
}
