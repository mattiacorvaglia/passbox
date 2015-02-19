package com.mcdev.passbox.ui;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.LoginDao;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.content.RecoveryDto;
import com.mcdev.passbox.utils.Constants;
import com.mcdev.passbox.views.FloatingActionButton;
import com.mcdev.passbox.views.colorpicker.ColorPickerDialog;
import com.mcdev.passbox.views.colorpicker.ColorPickerSwatch;
import com.mcdev.passbox.utils.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@SuppressLint("ClickableViewAccessibility")
public class PasswordDetailActivity extends ActionBarActivity {
	
	private Context mContext;
	private long pwdId;
	private RelativeLayout usernameLayout, passwordLayout, webUrlLayout;
	private LinearLayout descriptionLayout, recoveryContainer;
	private View descriptionDivider;
	private TextView username, webUrl, description;
	private EditText password;
	private List<EditText> editTextToToggleVisibility;
	private Map<String, Integer> colorSet;
	private Toolbar toolbar;
	private FloatingActionButton togglePasswordVisibility;
    private ImageButton openLink;
	private int mSelectedColor = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get data from the calling intent
		Bundle intentExtras = getIntent().getExtras();
		if (intentExtras == null) {
			Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving extras from the calling intent");
			Toast.makeText(this, "Error on retrieving extras from the calling intent", Toast.LENGTH_SHORT).show();
			finish();
            return;
		}
		pwdId = intentExtras.getLong(Constants.TAG_EXTRA_PASSWORD_ID);
		String pwdTitle = intentExtras.getString(Constants.TAG_EXTRA_PASSWORD_TITLE);
		String pwdColor = intentExtras.getString(Constants.TAG_EXTRA_PASSWORD_COLOR);
		
		// Initialize the context reference
		mContext = this;
		
		colorSet = Util.Colors.getColorSet(mContext.getResources(), pwdColor);
				
		// set the status bar color
		if (Build.VERSION.SDK_INT >= 21) {
	        getWindow().setStatusBarColor(colorSet.get(Util.Colors.KEY_COLOR_PRIMARY_DARK));
		}
		
		// Set the content View
		setContentView(R.layout.activity_detail_password);
		
		// Toolbar
		toolbar = (Toolbar) findViewById(R.id.toolbar_extended);
		setSupportActionBar(toolbar);
		toolbar.setBackgroundColor(colorSet.get(Util.Colors.KEY_COLOR_PRIMARY));
		
		// Set the title
		setTitle(pwdTitle);
		
		// Enable back button in the Toolbar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Inflating
		recoveryContainer 	= (LinearLayout) findViewById(R.id.pwd_detail_recovery_container);
		usernameLayout 		= (RelativeLayout) findViewById(R.id.pwd_detail_username_layout);
		passwordLayout 		= (RelativeLayout) findViewById(R.id.pwd_detail_password_layout);
		webUrlLayout 		= (RelativeLayout) findViewById(R.id.pwd_detail_weburl_layout);
		descriptionLayout 	= (LinearLayout) findViewById(R.id.pwd_detail_description_layout);
		descriptionDivider 	= findViewById(R.id.pwd_detail_description_divider);
		
		username 	= (TextView) findViewById(R.id.pwd_detail_username);
		password 	= (EditText) findViewById(R.id.pwd_detail_pwd);
		webUrl 		= (TextView) findViewById(R.id.pwd_detail_web_url);
		description = (TextView) findViewById(R.id.pwd_detail_description);
        
        openLink                 = (ImageButton) findViewById(R.id.action_open_link);
		togglePasswordVisibility = (FloatingActionButton) findViewById(R.id.toggle_pwd_visibility);
        
		togglePasswordVisibility.setColorNormal(colorSet.get(Util.Colors.KEY_COLOR_ACCENT));
        
		// Add the password EditText in the list
		editTextToToggleVisibility = new ArrayList<>();
		editTextToToggleVisibility.add(password);
				
		// Fill contents
		new UpdateUI().execute();
		
		/*
		 * Toggle the visibility of the password field
		 */
		togglePasswordVisibility.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eventaction = event.getAction();
			    switch (eventaction) {
			        case MotionEvent.ACTION_DOWN: 
			        	// Show password
			        	for (EditText mEditText : editTextToToggleVisibility) {
			        		mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			        	}
			        	break;
			        case MotionEvent.ACTION_UP: 
			        	// Hide password
			        	for (EditText mEditText : editTextToToggleVisibility) {
			        		mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			        	}
			        	break;
					default:
						break;
				}
				return true;
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_password_detail, menu);
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
				updatePassword();
				return true;
			case R.id.action_remove_password:
				removePassword();
				return true;
			case R.id.action_change_color:
				showColorPicker();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case Constants.REQUEST_CODE_UPDATE_PASSWORD:
                    new UpdateUI().execute();
					// Update UI in the password collection fragment
					setResult(RESULT_OK);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Get the content from the database and
	 * update the UI with an async task
	 */
    private class UpdateUI extends AsyncTask<String, Void, PasswordDto> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recoveryContainer.removeAllViews();
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
                Log.w(Constants.TAG_APPLICATION_LOG, "Error on retrieving the password detail from the database or on decrypting the stored password");
                Toast.makeText(mContext, getString(R.string.error_decryption), Toast.LENGTH_SHORT).show();
                finish();
            } else {

                // Set title
                setTitle(result.getTitle());

                // Set username
                String pwdUsername = result.getUsername();
                if (Util.Strings.isNullOrEmpty(pwdUsername)) {
                    usernameLayout.setVisibility(View.GONE);
                } else {
                    usernameLayout.setVisibility(View.VISIBLE);
                    username.setText(pwdUsername);
                }

                // Set password
                final String pwdPassword = result.getPassword();
                if (Util.Strings.isNullOrEmpty(pwdPassword)) {
                    passwordLayout.setVisibility(View.GONE);
                } else {
                    passwordLayout.setVisibility(View.VISIBLE);
                    password.setText(pwdPassword);
                    // Disable the EditText default style
                    password.setOnTouchListener(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // Copy password in the clipboard
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("password", pwdPassword);
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(mContext, getString(R.string.action_clipdata_copied), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                    password.setFocusable(false);
                }

                // Set web URL
                final String pwdWebUrl = result.getWebUrl();
                if (Util.Strings.isNullOrEmpty(pwdWebUrl)) {
                    webUrlLayout.setVisibility(View.GONE);
                } else {
                    webUrlLayout.setVisibility(View.VISIBLE);
                    webUrl.setText(pwdWebUrl);
                    openLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = pwdWebUrl;
                            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                                url = "http://" + url;
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        }
                    });
                }

                // Set description
                String pwdDescription = result.getDescription();
                if (Util.Strings.isNullOrEmpty(pwdDescription)) {
                    descriptionLayout.setVisibility(View.GONE);
                    descriptionDivider.setVisibility(View.GONE);
                } else {
                    descriptionLayout.setVisibility(View.VISIBLE);
                    descriptionDivider.setVisibility(View.VISIBLE);
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
	 * Add a recovery entry layout
	 */
	private void addRecoveryEntry(RecoveryDto recv) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mView = inflater.inflate(R.layout.item_recovery_detail, recoveryContainer, false);
		
		// Add the recovery entry text in the Map
		TextView mQuestionTextView = (TextView) mView.findViewById(R.id.pwd_detail_question);
		EditText mAnswerEditText = (EditText) mView.findViewById(R.id.pwd_detail_answer);
		
		mQuestionTextView.setText(recv.getQuestion());
		mAnswerEditText.setText(recv.getAnswer());
		
		// Disable the EditText default style
		mAnswerEditText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mAnswerEditText.setFocusable(false);
		
		// Add the answer EditText in the list
		editTextToToggleVisibility.add(mAnswerEditText);
		
	    // Add the inflated view
		recoveryContainer.addView(mView);
	}
	
	/**
	 * Delete this password in the database
	 */
	private void updatePassword() {
		Intent mIntent = new Intent(mContext, UpdatePasswordActivity.class);
		mIntent.putExtra(Constants.TAG_EXTRA_PASSWORD_ID, pwdId);
		startActivityForResult(mIntent, Constants.REQUEST_CODE_UPDATE_PASSWORD);
	}
	
	/**
	 * Delete this password in the database
	 */
	private void removePassword() {
		PasswordDao.getInstance(mContext).open();
		PasswordDao.getInstance(mContext).deletePassword(pwdId);
		PasswordDao.getInstance(mContext).close();
		
		setResult(RESULT_OK);
		finish();
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
			@SuppressLint("NewApi")
			@Override
			public void onColorSelected(int color) {
				mSelectedColor = color;
				String stringColor = Util.Colors.toString(mSelectedColor);
				colorSet = Util.Colors.getColorSet(mContext.getResources(), stringColor);
				toolbar.setBackgroundColor(colorSet.get(Util.Colors.KEY_COLOR_PRIMARY));
				togglePasswordVisibility.setColorNormal(colorSet.get(Util.Colors.KEY_COLOR_ACCENT));
				if (Build.VERSION.SDK_INT >= 21) {
			        getWindow().setStatusBarColor(colorSet.get(Util.Colors.KEY_COLOR_PRIMARY_DARK));
				}
				PasswordDao.getInstance(mContext).open();
				int affectedRows = PasswordDao.getInstance(mContext).updatePasswordColor(pwdId, stringColor);
				PasswordDao.getInstance(mContext).close();
				
				if (affectedRows != 1) {
					Toast.makeText(mContext, "Error on updating the color", Toast.LENGTH_SHORT).show();
				} else {
					setResult(RESULT_OK);
				}
			}
		});
		colorcalendar.show(getSupportFragmentManager(), "colorPicker");
	}

}
