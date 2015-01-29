package com.mcdev.passbox.ui;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import android.content.Context;
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

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.content.RecoveryDao;
import com.mcdev.passbox.content.RecoveryDto;
import com.mcdev.passbox.utils.CommonResources;

public class UpdatePasswordActivity extends ActionBarActivity {
	
	private Context mContext;
	private long pwdId;
	private String currentColor;
	private LinearLayout recoveryContainer;
	private LinkedHashMap<EditText, EditText> recoveryEditTextList;
	private EditText title, username, password, webUrl, description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_password);
		
		mContext = this;
		
		// Toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Enable back button in the Toolbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get data from the calling intent
		Bundle intentExtras = getIntent().getExtras();
		if (intentExtras == null) {
			Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on retrieving extras from the calling intent");
			Toast.makeText(this, "Error on retrieving extras from the calling intent", Toast.LENGTH_SHORT).show();
			finish();
		}
		pwdId = intentExtras.getLong(CommonResources.TAG_EXTRA_PASSWORD_ID);
		
		// Inflating
		recoveryContainer = (LinearLayout) findViewById(R.id.pwd_update_recovery_container);
		title 		= (EditText) findViewById(R.id.pwd_update_title);
		username 	= (EditText) findViewById(R.id.pwd_update_username);
		password 	= (EditText) findViewById(R.id.pwd_update_pwd);
		webUrl 		= (EditText) findViewById(R.id.pwd_update_web_url);
		description = (EditText) findViewById(R.id.pwd_update_description);
		
		// Initialize the Map of EditText for the recovery entries
		recoveryEditTextList = new LinkedHashMap<EditText, EditText>();
		
		// fill the form with the current values
		updateUI();
		
		// Set the listener for the add button
		Button addRecovery = (Button) findViewById(R.id.pwd_update_recovery);
		addRecovery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Add view
				addNewRecoveryEntry();
			}
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_password_update, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				finish();
				return true;
			case R.id.action_update_done:
				updatePassword();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Update the UI with the contents
	 */
	private void updateUI() {
		
		PasswordDao.getInstance(mContext).open();
		PasswordDto pwd = PasswordDao.getInstance(mContext).getPassword(pwdId);
		PasswordDao.getInstance(mContext).close();
		
		if (pwd == null) {
			Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on retrieving the password detail from the database");
			Toast.makeText(mContext, "Wrong password id", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			
			// Set color
			currentColor = pwd.getColor();
			
			// Set title
			String pwdTitle = pwd.getTitle();
			if (pwdTitle == null || pwdTitle.length() < 1) {
				Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on retrieving the password title");
			} else {
				title.setText(pwdTitle);
			}
			
			// Set username
			String pwdUsername = pwd.getUsername();
			if (pwdUsername != null && pwdUsername.length() > 0) {
				username.setText(pwdUsername);
			}
			
			// Set title
			String pwdPassword = pwd.getPassword();
			if (pwdPassword == null || pwdPassword.length() < 1) {
				Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on retrieving the password");
			} else {
				password.setText(pwdPassword);
			}
			
			// Set web URL
			String pwdWebUrl = pwd.getWebUrl();
			if (pwdWebUrl != null && pwdWebUrl.length() > 0) {
				webUrl.setText(pwdWebUrl);
			}
			
			// Set description
			String pwdDescription = pwd.getDescription();
			if (pwdDescription != null && pwdDescription.length() > 0) {
				description.setText(pwdDescription);
			}
			
			// Set recovery
			LinkedList<RecoveryDto> pwdRecoveryList = pwd.getRecoveryList();
			if (pwdRecoveryList != null && pwdRecoveryList.size() > 0) {
				// Iterate each recovery entry
				for (RecoveryDto mRecovery : pwdRecoveryList) {
					// Add view
					addRecoveryEntry(mRecovery);
				}
			}
			
		}
	}
	
	/**
	 * Add a new recovery entry layout
	 */
	private void addNewRecoveryEntry() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mView = inflater.inflate(R.layout.item_recovery_update, recoveryContainer, false);
		
		// Add the recovery entry text in the Map
		final EditText mQuestionEditText = (EditText) mView.findViewById(R.id.pwd_update_question);
		EditText mAnswerEditText = (EditText) mView.findViewById(R.id.pwd_update_answer);
		recoveryEditTextList.put(mQuestionEditText, mAnswerEditText);
		
		// Set the remove button listener
		Button removeRecovery = (Button) mView.findViewById(R.id.pwd_update_remove_recovery);
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
	 * Add an existing recovery entry layout
	 */
	private void addRecoveryEntry(final RecoveryDto recv) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mView = inflater.inflate(R.layout.item_recovery_update, recoveryContainer, false);
		
		// Add the recovery entry text in the Map
		final EditText mQuestionEditText = (EditText) mView.findViewById(R.id.pwd_update_question);
		EditText mAnswerEditText = (EditText) mView.findViewById(R.id.pwd_update_answer);
		
		mQuestionEditText.setText(recv.getQuestion());
		mQuestionEditText.setTag(recv.getId());
		mAnswerEditText.setText(recv.getAnswer());
		
		recoveryEditTextList.put(mQuestionEditText, mAnswerEditText);
		
		// Set the remove button listener
		Button removeRecovery = (Button) mView.findViewById(R.id.pwd_update_remove_recovery);
		removeRecovery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recoveryContainer.removeView(mView);
				recoveryEditTextList.remove(mQuestionEditText);
				RecoveryDao.getInstance(mContext).open(mContext);
				RecoveryDao.getInstance(mContext).deleteRecovery(recv.getId());
				RecoveryDao.getInstance(mContext).close();
				setResult(RESULT_OK);
			}
		});
		
	    // Add the inflated view
		recoveryContainer.addView(mView);
	}
	
	/**
	 * Update the existing password in the database
	 */
	private void updatePassword() {
		
		// Initialize a new password
		PasswordDto mPassword = new PasswordDto();
		mPassword.setId(pwdId);
		
		// Check not null title
		String insertedTitle = title.getText().toString();
		if (insertedTitle == null || insertedTitle.length() < 1) {
			title.setError(getResources().getString(R.string.error_add_title));
		} else {
			mPassword.setTitle(insertedTitle);
			// Check not null password
			String insertedPwd = password.getText().toString();
			if (insertedPwd == null || insertedPwd.length() < 1) {
				password.setError(getResources().getString(R.string.error_add_pwd));
			} else {
				mPassword.setPassword(insertedPwd);
				// Check not null username
				String insertedUsername = username.getText().toString();
				if (insertedUsername != null && insertedUsername.length() > 0) {
					mPassword.setUsername(insertedUsername);
				}
				// Check not null web URL
				String insertedWebUrl = webUrl.getText().toString();
				if (insertedWebUrl != null && insertedWebUrl.length() > 0) {
					mPassword.setWebUrl(insertedWebUrl);
				}
				// Check not null description
				String insertedDescription = description.getText().toString();
				if (insertedDescription != null && insertedDescription.length() > 0) {
					mPassword.setDescription(insertedDescription);
				}
				// Check not null color
				mPassword.setColor(currentColor);
				
				// Create the recovery list
				boolean RecoveryDone = false;
				if (recoveryEditTextList == null || recoveryEditTextList.size() < 1) {
					RecoveryDone = true;
				} else {
					for (EditText mQuestion : recoveryEditTextList.keySet()) {
						RecoveryDto mRecovery = new RecoveryDto();
				    	String insertedQuestion = mQuestion.getText().toString();
				    	// Check not null question
				    	if (insertedQuestion == null || insertedQuestion.length() < 1) {
				    		mQuestion.setError(getResources().getString(R.string.error_add_question));
				    		RecoveryDone = false;
				    	} else {
				    		mRecovery.setQuestion(insertedQuestion);
				    		// Set the Id of the recovery entry if just existing
				    		if (mQuestion.getTag() != null) {
				    			mRecovery.setId((Long) mQuestion.getTag());
				    		}
				    		// Check not null answer
				    		EditText mAnswer = recoveryEditTextList.get(mQuestion);
				    		String insertedAnswer = mAnswer.getText().toString();
				    		if (insertedAnswer == null || insertedAnswer.length() < 1) {
				    			mAnswer.setError(getResources().getString(R.string.error_add_answer));
				    			RecoveryDone = false;
				    		} else {
				    			mRecovery.setAnswer(insertedAnswer);
				    			mRecovery.setPasswordKey(pwdId);
				    			mPassword.addRecovery(mRecovery);
				    			RecoveryDone = true;
				    		}
				    	}
				    }
				}
				
				if (RecoveryDone) {
					// Store in the database
					PasswordDao.getInstance(mContext).open();
					int affectedRows = PasswordDao.getInstance(mContext).updatePassword(mPassword);
					PasswordDao.getInstance(mContext).close();
					
					if (affectedRows != 1) {
						Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on updating the password in the database");
						Toast.makeText(mContext, "Error on updating the password in the database", Toast.LENGTH_SHORT).show();
					} else {
						// Close this screen
						setResult(RESULT_OK);
						finish();
					}
				}
			}
			
		}
		
	}

}
