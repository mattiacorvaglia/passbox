package com.mcdev.passbox.utils;

import com.mcdev.passbox.ui.LoginActivity;
import com.mcdev.passbox.ui.SetLoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Loginer {

	private SharedPreferences pref;
	private Editor editor;
	private Context context;
	private static final int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME       = "PassboxSharedPreferences";
	private static final String IS_FIRST_TIME   = "IsFirstTime";
	private static final String KEY_PASSWORD    = "AuthCode";
	
	private static Loginer instance = null;
	
    // Constructor
	protected Loginer(Context mContext) {
		this.context = mContext;
		this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		this.editor = extracted();
	}
	
	// Singleton
	public static Loginer getInstance(Context mContext) {
		if (instance == null) {
			instance = new Loginer(mContext);
		}
		return instance;
	}

	/**
	 * Get the extracted editor
	 * @return The editor
	 */
	private Editor extracted() {
		return pref.edit();
	}
	
	/**
	 * Extract the fist-time status from the SharedPreferences
	 * @return true if this is the first time th app
     *          is launched after the installation
	 */
	private boolean isFirstTime() {
		return pref.getBoolean(IS_FIRST_TIME, true);
	}
	
	/**
	 * Check if is the first time the app is launched
	 */
	public void checkLogin() {

		// Check login status
		if(isFirstTime()) {
			Intent i = new Intent(context, SetLoginActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			LoginActivity.loginActivity.finish();
		}
		
	}
	
	/**
	 * Set the password
	 */
	public void setLogin(String pwd) {
		
		// Store isFirstTime value as false
		editor.putBoolean(IS_FIRST_TIME, false);
		
		// Store the password
		editor.putString(KEY_PASSWORD, pwd);
		
		// Save changes
		editor.commit();
	
	}
	
	/**
	 * Get the stored password
	 * @return The main password
	 */
	public String getMainPwd() {
		return pref.getString(KEY_PASSWORD, null);
	}
	
}
