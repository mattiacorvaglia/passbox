package com.mcdev.passbox.content;

import android.provider.BaseColumns;

public final class PassboxContract {

	// Empty Constructor
	public PassboxContract() {}
	
	/**
	 * Password DB Table
	 */
	public static abstract class PasswordEntry implements BaseColumns {
		
		public static final String TABLE_NAME 				= "password";
		public static final String COLUMN_NAME_ENTRY_ID 	= "password_id";
		public static final String COLUMN_NAME_TITLE 		= "title";
		public static final String COLUMN_NAME_DESCRIPTION 	= "description";
		public static final String COLUMN_NAME_USERNAME 	= "username";
		public static final String COLUMN_NAME_PASSWORD 	= "password";
		public static final String COLUMN_NAME_WEB_URL 		= "web_url";
		public static final String COLUMN_NAME_COLOR 		= "color";
		
	}
	
	/**
	 * Recovery DB Table
	 */
	public static abstract class RecoveryEntry implements BaseColumns {
		
		public static final String TABLE_NAME 				= "recovery";
		public static final String COLUMN_NAME_ENTRY_ID 	= "recovery_id";
		public static final String COLUMN_NAME_QUESTION		= "question";
		public static final String COLUMN_NAME_ANSWER	 	= "answer";
		public static final String COLUMN_NAME_PASSWORD_KEY	= "password_key";
		
	}

}
