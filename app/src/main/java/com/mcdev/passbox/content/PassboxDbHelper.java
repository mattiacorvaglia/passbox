package com.mcdev.passbox.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PassboxDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Passbox.db";
    
    private static final String PRIMARY_KEY_TYPE        = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String FOREIGN_KEY_TYPE        = " FOREIGN KEY (";
    private static final String FOREIGN_KEY_REFERENCES  = ") REFERENCES ";
    private static final String FOREIGN_KEY_ON_DELETE   = "ON DELETE CASCADE";
    private static final String TEXT_TYPE               = " TEXT";
    private static final String INTEGER_TYPE            = " INTEGER";
    private static final String COMMA_SEP               = ", ";
    private static final String SQL_CREATE_PASSWORD_ENTRIES =
            "CREATE TABLE " +
    		PassboxContract.PasswordEntry.TABLE_NAME + " (" +
    		PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID 		+ PRIMARY_KEY_TYPE 	+ COMMA_SEP +
    		PassboxContract.PasswordEntry.COLUMN_NAME_TITLE 		+ TEXT_TYPE 		+ COMMA_SEP +
            PassboxContract.PasswordEntry.COLUMN_NAME_DESCRIPTION 	+ TEXT_TYPE 		+ COMMA_SEP +
            PassboxContract.PasswordEntry.COLUMN_NAME_USERNAME 		+ TEXT_TYPE			+ COMMA_SEP +
            PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD 		+ TEXT_TYPE 		+ COMMA_SEP +
            PassboxContract.PasswordEntry.COLUMN_NAME_WEB_URL 		+ TEXT_TYPE 		+ COMMA_SEP +
            PassboxContract.PasswordEntry.COLUMN_NAME_COLOR 		+ TEXT_TYPE			+ ");";
    private static final String SQL_CREATE_RECOVERY_ENTRIES =
            "CREATE TABLE " +
    		PassboxContract.RecoveryEntry.TABLE_NAME + "(" +
    		PassboxContract.RecoveryEntry.COLUMN_NAME_ENTRY_ID 		+ PRIMARY_KEY_TYPE 	+ COMMA_SEP +
    		PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION 		+ TEXT_TYPE 		+ COMMA_SEP +
    		PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER		+ TEXT_TYPE			+ COMMA_SEP +
    		PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY	+ INTEGER_TYPE		+ COMMA_SEP +
    		FOREIGN_KEY_TYPE + PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY	+ FOREIGN_KEY_REFERENCES +
    		PassboxContract.PasswordEntry.TABLE_NAME + " (" + PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID +
    		") " + FOREIGN_KEY_ON_DELETE + ");";
    private static final String SQL_CREATE_LOGIN_ENTRIES =
            "CREATE TABLE " +
                    PassboxContract.LoginEntry.TABLE_NAME + "(" +
                    PassboxContract.LoginEntry.COLUMN_NAME_ENTRY_ID 		+ PRIMARY_KEY_TYPE 	+ COMMA_SEP +
                    PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD 		+ TEXT_TYPE 		+ COMMA_SEP +
                    PassboxContract.LoginEntry.COLUMN_NAME_LABEL     		+ TEXT_TYPE 		+ ");";
    private static final String SQL_DELETE_PASSWORD_ENTRIES =
            "DROP TABLE IF EXISTS " + PassboxContract.PasswordEntry.TABLE_NAME + ";";
    private static final String SQL_DELETE_RECOVERY_ENTRIES =
            "DROP TABLE IF EXISTS " + PassboxContract.RecoveryEntry.TABLE_NAME + ";";
    private static final String SQL_DELETE_LOGIN_ENTRIES =
            "DROP TABLE IF EXISTS " + PassboxContract.LoginEntry.TABLE_NAME + ";";
    
    // Constructor
    public PassboxDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PASSWORD_ENTRIES);
        db.execSQL(SQL_CREATE_RECOVERY_ENTRIES);
        db.execSQL(SQL_CREATE_LOGIN_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PASSWORD_ENTRIES);
        db.execSQL(SQL_DELETE_RECOVERY_ENTRIES);
        db.execSQL(SQL_DELETE_LOGIN_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    
}
