package com.mcdev.passbox.ui;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

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

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.LoginDao;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.content.RecoveryDao;
import com.mcdev.passbox.content.RecoveryDto;
import com.mcdev.passbox.utils.Constants;
import com.mcdev.passbox.utils.Loginer;
import com.mcdev.passbox.utils.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
			Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving extras from the calling intent");
			Toast.makeText(this, "Error on retrieving extras from the calling intent", Toast.LENGTH_SHORT).show();
			finish();
		}
		pwdId = intentExtras.getLong(Constants.TAG_EXTRA_PASSWORD_ID);
		
		// Inflating
		recoveryContainer = (LinearLayout) findViewById(R.id.pwd_update_recovery_container);
		title 		= (EditText) findViewById(R.id.pwd_update_title);
		username 	= (EditText) findViewById(R.id.pwd_update_username);
		password 	= (EditText) findViewById(R.id.pwd_update_pwd);
		webUrl 		= (EditText) findViewById(R.id.pwd_update_web_url);
		description = (EditText) findViewById(R.id.pwd_update_description);
		
		// Initialize the Map of EditText for the recovery entries
		recoveryEditTextList = new LinkedHashMap<>();
		
		// fill the form with the current values
		new UpdateUI().execute();
		
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
	 * Update the UI with the contents retrieved
     * from the database with an async task
	 */
    private class UpdateUI extends AsyncTask<String, Void, PasswordDto> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected PasswordDto doInBackground(String... params) {
            /**
             * Decrypt the stored password
             */
            try {
                // Get data from the database
                PasswordDao.getInstance(mContext).open();
                PasswordDto pwd = PasswordDao.getInstance(mContext).getPassword(pwdId);
                PasswordDao.getInstance(mContext).close();
                
                if (pwd == null) {
                    return null;
                } else {
//                    String passphrase = Loginer.getInstance(mContext).getMainPwd();
                    LoginDao.getInstance(mContext).open();
                    String passphrase = LoginDao.getInstance(mContext).getLogin();
                    LoginDao.getInstance(mContext).close();
                    if (Util.Strings.isNullOrEmpty(passphrase)) {
                        Toast.makeText(mContext, getString(R.string.error_get_login), Toast.LENGTH_SHORT).show();
                        return null;
                    } else {
                        String decrypted = Util.Crypto.decrypt(pwd.getPassword(), passphrase);
                        pwd.setPassword(decrypted);
                        return pwd;
                    }
                }

            } catch (NoSuchPaddingException | IllegalBlockSizeException |
                    BadPaddingException | InvalidKeyException |
                    InvalidAlgorithmParameterException | UnsupportedEncodingException |
                    InvalidKeySpecException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(PasswordDto result) {
            super.onPostExecute(result);

            if (result == null) {
                Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving the password detail from the database");
                Toast.makeText(mContext, getString(R.string.error_decryption), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Set color
                currentColor = result.getColor();

                // Set title
                String pwdTitle = result.getTitle();
                if (Util.Strings.isNullOrEmpty(pwdTitle)) {
                    Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving the password title");
                } else {
                    title.setText(pwdTitle);
                }

                // Set username
                String pwdUsername = result.getUsername();
                if (!Util.Strings.isNullOrEmpty(pwdUsername)) {
                    username.setText(pwdUsername);
                }

                // Set title
                String pwdPassword = result.getPassword();
                if (Util.Strings.isNullOrEmpty(pwdPassword)) {
                    Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving the password");
                } else {
                    password.setText(pwdPassword);
                }

                // Set web URL
                String pwdWebUrl = result.getWebUrl();
                if (!Util.Strings.isNullOrEmpty(pwdWebUrl)) {
                    webUrl.setText(pwdWebUrl);
                }

                // Set description
                String pwdDescription = result.getDescription();
                if (!Util.Strings.isNullOrEmpty(pwdDescription)) {
                    description.setText(pwdDescription);
                }

                // Set recovery
                LinkedList<RecoveryDto> pwdRecoveryList = result.getRecoveryList();
                if (pwdRecoveryList != null && pwdRecoveryList.size() > 0) {
                    // Iterate each recovery entry
                    for (RecoveryDto mRecovery : pwdRecoveryList) {
                        // Add view
                        addRecoveryEntry(mRecovery);
                    }
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
		if (Util.Strings.isNullOrEmpty(insertedTitle)) {
			title.setError(getResources().getString(R.string.error_add_title));
		} else {
			mPassword.setTitle(insertedTitle);
			// Check not null password
			String insertedPwd = password.getText().toString();
			if (Util.Strings.isNullOrEmpty(insertedPwd)) {
				password.setError(getResources().getString(R.string.error_add_pwd));
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
				    	if (Util.Strings.isNullOrEmpty(insertedQuestion)) {
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
				    		if (Util.Strings.isNullOrEmpty(insertedAnswer)) {
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
                    new RestorePassword(mPassword).execute();
				}
			}
		}
	}

    /**
     * Async Task used to encrypt password
     * and restore it in the database
     */
    private class RestorePassword extends AsyncTask<String, Void, Integer> {

        private PasswordDto mPassword;

        // Constructor
        private RestorePassword(PasswordDto mPassword) {
            this.mPassword = mPassword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            /**
             * Encrypt password before storing it in the database
             */
            try {
//                String passphrase = Loginer.getInstance(mContext).getMainPwd();
                LoginDao.getInstance(mContext).open();
                String passphrase = LoginDao.getInstance(mContext).getLogin();
                LoginDao.getInstance(mContext).close();
                if (Util.Strings.isNullOrEmpty(passphrase)) {
                    Toast.makeText(mContext, getString(R.string.error_get_login), Toast.LENGTH_SHORT).show();
                    return null;
                } else {
                    String encrypted = Util.Crypto.encrypt(mPassword.getPassword(), passphrase);
                    mPassword.setPassword(encrypted);

                    // Store in the database
                    PasswordDao.getInstance(mContext).open();
                    int affectedRows = PasswordDao.getInstance(mContext).updatePassword(mPassword);
                    PasswordDao.getInstance(mContext).close();

                    return affectedRows;
                }
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException |
                    BadPaddingException | InvalidKeyException |
                    InvalidAlgorithmParameterException | NoSuchPaddingException |
                    InvalidKeySpecException | UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.w(Constants.TAG_APPLICATION_LOG, "Error on encrypting the new password");
                Toast.makeText(mContext, getString(R.string.error_encryption), Toast.LENGTH_SHORT).show();
            } else {
                if (result != 1) {
                    Log.w(Constants.TAG_APPLICATION_LOG, "Error on updating the password in the database or on decrypting the password");
                    Toast.makeText(mContext, getString(R.string.error_storing), Toast.LENGTH_SHORT).show();
                } else {
                    // Close this screen
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

}
