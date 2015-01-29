package com.mcdev.passbox.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.content.RecoveryDto;
import com.mcdev.passbox.utils.CommonResources;
import com.mcdev.passbox.utils.FloatingActionButton;
import com.mcdev.passbox.utils.colorpicker.ColorPickerDialog;
import com.mcdev.passbox.utils.colorpicker.ColorPickerSwatch;
import com.mcdev.passbox.utils.colorpicker.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class PasswordDetailActivity extends ActionBarActivity {
	
	private static final int REQUEST_CODE_UPDATE_PASSWORD = 125;
	
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
	
	private int mSelectedColor = 0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get data from the calling intent
		Bundle intentExtras = getIntent().getExtras();
		if (intentExtras == null) {
			Log.w(CommonResources.TAG_APPLICATION_LOG, "Error on retrieving extras from the calling intent");
			Toast.makeText(this, "Error on retrieving extras from the calling intent", Toast.LENGTH_SHORT).show();
			finish();
		}
		pwdId = intentExtras.getLong(CommonResources.TAG_EXTRA_PASSWORD_ID);
		String pwdTitle = intentExtras.getString(CommonResources.TAG_EXTRA_PASSWORD_TITLE);
		String pwdColor = intentExtras.getString(CommonResources.TAG_EXTRA_PASSWORD_COLOR);
		
		// Initialize the context reference
		mContext = this;
		
		colorSet = Utils.ColorUtils.getColorSet(mContext.getResources(), pwdColor);
				
		// set the status bar color
		if (Build.VERSION.SDK_INT >= 21) {
	        getWindow().setStatusBarColor(colorSet.get(Utils.ColorUtils.KEY_COLOR_PRIMARY_DARK));
		}
		
		// Set the content View
		setContentView(R.layout.activity_detail_password);
		
		// Toolbar
		toolbar = (Toolbar) findViewById(R.id.toolbar_extended);
		setSupportActionBar(toolbar);
		toolbar.setBackgroundColor(colorSet.get(Utils.ColorUtils.KEY_COLOR_PRIMARY));
		
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
		descriptionDivider 	= (View) findViewById(R.id.pwd_detail_description_divider);
		
		username 	= (TextView) findViewById(R.id.pwd_detail_username);
		password 	= (EditText) findViewById(R.id.pwd_detail_pwd);
		webUrl 		= (TextView) findViewById(R.id.pwd_detail_web_url);
		description = (TextView) findViewById(R.id.pwd_detail_description);
		
		togglePasswordVisibility = (FloatingActionButton) findViewById(R.id.toggle_pwd_visibility);
		togglePasswordVisibility.setColorNormal(colorSet.get(Utils.ColorUtils.KEY_COLOR_ACCENT));
		
		// Add the password EditText in the list
		editTextToToggleVisibility = new ArrayList<EditText>();
		editTextToToggleVisibility.add(password);
				
		// Fill contents
		updateUI();
		
		// Disable the EditText default style
		password.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO make copy on clipboard
				return true;
			}
		});
		password.setFocusable(false);
		
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
				case REQUEST_CODE_UPDATE_PASSWORD:
					updateUI();
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
	 * update the UI
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
			
			// Set title
			setTitle(pwd.getTitle());
			
			// Set username
			String pwdUsername = pwd.getUsername();
			if (pwdUsername == null || pwdUsername.length() < 1) {
				usernameLayout.setVisibility(View.GONE);
			} else {
				usernameLayout.setVisibility(View.VISIBLE);
				username.setText(pwdUsername);
			}
			
			// Set password
			String pwdPassword = pwd.getPassword();
			if (pwdPassword == null || pwdPassword.length() < 1) {
				passwordLayout.setVisibility(View.GONE);
			} else {
				passwordLayout.setVisibility(View.VISIBLE);
				password.setText(pwdPassword);
			}
			
			// Set web URL
			String pwdWebUrl = pwd.getWebUrl();
			if (pwdWebUrl == null || pwdWebUrl.length() < 1) {
				webUrlLayout.setVisibility(View.GONE);
			} else {
				webUrlLayout.setVisibility(View.VISIBLE);
				webUrl.setText(pwdWebUrl);
			}
			
			// Set description
			String pwdDescription = pwd.getDescription();
			if (pwdDescription == null || pwdDescription.length() < 1) {
				descriptionLayout.setVisibility(View.GONE);
				descriptionDivider.setVisibility(View.GONE);
			} else {
				descriptionLayout.setVisibility(View.VISIBLE);
				descriptionDivider.setVisibility(View.VISIBLE);
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
				// TODO make copy on clipboard
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
		mIntent.putExtra(CommonResources.TAG_EXTRA_PASSWORD_ID, pwdId);
		startActivityForResult(mIntent, REQUEST_CODE_UPDATE_PASSWORD);
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
		int[] mColorChoices = Utils.ColorUtils.colorChoice(this);
		ColorPickerDialog colorcalendar = ColorPickerDialog.newInstance(
				R.string.color_picker_default_title, 	// Dialog title
				mColorChoices,							// List of colors choices
				mSelectedColor,							// Actual selected color
				4,										// Columns
				(Utils.isTablet(this)) ? ColorPickerDialog.SIZE_LARGE : ColorPickerDialog.SIZE_SMALL);	// Size of the screen

		// Implement listener to get selected color value
		colorcalendar.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
			@SuppressLint("NewApi")
			@Override
			public void onColorSelected(int color) {
				mSelectedColor = color;
				String stringColor = Utils.ColorUtils.toString(mSelectedColor);
				colorSet = Utils.ColorUtils.getColorSet(mContext.getResources(), stringColor);
				toolbar.setBackgroundColor(colorSet.get(Utils.ColorUtils.KEY_COLOR_PRIMARY));
				togglePasswordVisibility.setColorNormal(colorSet.get(Utils.ColorUtils.KEY_COLOR_ACCENT));
				if (Build.VERSION.SDK_INT >= 21) {
			        getWindow().setStatusBarColor(colorSet.get(Utils.ColorUtils.KEY_COLOR_PRIMARY_DARK));
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
