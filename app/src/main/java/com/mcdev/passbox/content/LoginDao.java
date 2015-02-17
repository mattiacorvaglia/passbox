package com.mcdev.passbox.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mcdev.passbox.utils.Constants;
import com.mcdev.passbox.utils.Util;

/**
 * DAO methods for the login table
 * @author Mattia Corvaglia
 */
public class LoginDao {

    private SQLiteDatabase database;
    private PassboxDbHelper mDbHelper;
    private Context mContext;
    private static final String TAG_LABEL = "main-key";
    private String[] allColumns = {
            PassboxContract.LoginEntry.COLUMN_NAME_ENTRY_ID,
            PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD,
            PassboxContract.LoginEntry.COLUMN_NAME_LABEL
    };

    // Singleton
    private static LoginDao instance = null;

    // Constructor
    protected LoginDao(Context context) {
        mDbHelper = new PassboxDbHelper(context);
//		Log.i(Constants.TAG_APPLICATION_LOG, "new PassboxDbHelper operation executed in LoginDao class");
    }

    // Singleton Pattern
    public static LoginDao getInstance(Context context) {
        if (instance == null) {
            instance = new LoginDao(context);
        }
        return instance;
    }

    /**
     * Open DB connection
     *
     * @throws android.database.SQLException
     */
    public void open() throws SQLException {
        database = mDbHelper.getWritableDatabase();
//        Log.i(Constants.TAG_APPLICATION_LOG, "getWritableDatabase() operation executed in LoginDao class");
    }

    /**
     * Close DB connection
     */
    public void close() {
        mDbHelper.close();
//        Log.i(Constants.TAG_APPLICATION_LOG, "database close() operation executed in LoginDao class");
    }

    /**
     * Add the main password in the database
     * @param pwd The main password to insert
     * @return The id of the inserted password
     */
    public long insertLogin(String pwd) {

        ContentValues passwordValues = new ContentValues();
        passwordValues.put(PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD, Util.Strings.toMd5(pwd));
        passwordValues.put(PassboxContract.LoginEntry.COLUMN_NAME_LABEL,    TAG_LABEL);

        long newPasswordId = database.insert(
                PassboxContract.LoginEntry.TABLE_NAME,
                PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD,
                passwordValues
        );

        if (newPasswordId == -1) {
            Log.e(Constants.TAG_APPLICATION_LOG, "Error on insert a new main password in the database");
            throw new SQLException();
        }
        
        return newPasswordId;
    }

    /**
     * Delete the main password from the database
     */
    public void deleteLogin() {

        database.delete(PassboxContract.LoginEntry.TABLE_NAME, null, null);
        
    }

    /**
     * Update the main password in the database
     * @param newPwd The new main password to store
     * @return The number of affected rows
     */
    public int updateLogin(String newPwd) {

        String whereClause = PassboxContract.LoginEntry.COLUMN_NAME_LABEL + " = ?";
        String[] whereArgs = { TAG_LABEL };

        ContentValues values = new ContentValues();
        values.put(PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD, Util.Strings.toMd5(newPwd));
        values.put(PassboxContract.LoginEntry.COLUMN_NAME_LABEL, 	TAG_LABEL);

        int affectedRows = database.update(
                PassboxContract.LoginEntry.TABLE_NAME,
                values,
                whereClause,
                whereArgs
        );
//	    	Log.i(Constants.TAG_APPLICATION_LOG, "Affected rows = "+String.valueOf(affectedRows));
        if (affectedRows != 1) {
            Log.i(Constants.TAG_APPLICATION_LOG, "Error on update the main password in the database");
            throw new SQLException();
        }
        
        return affectedRows;

    }

    /**
     * Get the main password stored in the database
     * @return The main password
     */
    public String getLogin() {

        String whereClause = PassboxContract.LoginEntry.COLUMN_NAME_LABEL + " = ?";
        String[] whereArgs = { TAG_LABEL };

        // Get the password
        Cursor cursor = database.query(
                PassboxContract.LoginEntry.TABLE_NAME,	    // The table to query
                allColumns,                             	// The columns to return
                whereClause,                             	// The columns for the WHERE clause
                whereArgs,                               	// The values for the WHERE clause
                null,                                   	// Don't group the rows
                null,                                   	// Don't filter by row groups
                null                                    	// The sort order
        );

        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            String mainPwd = cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.LoginEntry.COLUMN_NAME_PASSWORD));
            cursor.close();
            return mainPwd;
        } else {
            cursor.close();
            Log.e(Constants.TAG_APPLICATION_LOG, "Founded multiple entries with the same password id");
            return null;
        }
        
    }
}
