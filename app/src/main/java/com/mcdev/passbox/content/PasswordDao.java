package com.mcdev.passbox.content;

import java.util.LinkedList;
import java.util.List;

import com.mcdev.passbox.utils.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PasswordDao {
	
	private SQLiteDatabase database;
    private PassboxDbHelper mDbHelper;
    private Context mContext;
    private String[] allColumns = {
		PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID,
		PassboxContract.PasswordEntry.COLUMN_NAME_TITLE,
		PassboxContract.PasswordEntry.COLUMN_NAME_DESCRIPTION,
		PassboxContract.PasswordEntry.COLUMN_NAME_USERNAME,
		PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD,
		PassboxContract.PasswordEntry.COLUMN_NAME_WEB_URL,
		PassboxContract.PasswordEntry.COLUMN_NAME_COLOR
    };
    private String[] liteColumns = {
		PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID,
		PassboxContract.PasswordEntry.COLUMN_NAME_TITLE,
		PassboxContract.PasswordEntry.COLUMN_NAME_COLOR
    };

    // Singleton
    private static PasswordDao instance = null;
	
	// Constructor
	protected PasswordDao(Context context) {
		mDbHelper = new PassboxDbHelper(context);
//		Log.i(Constants.TAG_APPLICATION_LOG, "new PassboxDbHelper operation executed in PasswordDao class");
	}
	
	// Singleton Pattern
	public static PasswordDao getInstance(Context context) {
		if (instance == null) {
			instance = new PasswordDao(context);
		}
		return instance;
	}
	
	/**
	 * Open DB connection
	 * 
	 * @throws SQLException
	 */
    public void open() throws SQLException {
        database = mDbHelper.getWritableDatabase();
//        Log.i(Constants.TAG_APPLICATION_LOG, "getWritableDatabase() operation executed in PasswordDao class");
    }

    /**
     * Close DB connection
     */
    public void close() {
        mDbHelper.close();
//        Log.i(Constants.TAG_APPLICATION_LOG, "database close() operation executed in PasswordDao class");
    }
    
    /**
     * Insert a new password in the database
     * 
     * @param pwd The password object to store
     * @return The id of the stored password
     */
    public long insertPassword(PasswordDto pwd) {
    	
    	long newPasswordId;
    	
    	// Execute all the insert queries in a transaction
    	database.beginTransaction();
//    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in PasswordDao class");
    	
    	try {
	    	/*
	    	 * Insert the password entry
	    	 */
	    	ContentValues passwordValues = new ContentValues();
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_TITLE, 		pwd.getTitle());
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_DESCRIPTION, 	pwd.getDescription());
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_USERNAME, 		pwd.getUsername());
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD, 		pwd.getPassword());
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_WEB_URL, 		pwd.getWebUrl());
	    	passwordValues.put(PassboxContract.PasswordEntry.COLUMN_NAME_COLOR, 		pwd.getColor());
	    	
	    	// Insert the new row, returning the primary key value of the new row
	        newPasswordId = database.insert(
	        	PassboxContract.PasswordEntry.TABLE_NAME,
	        	PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD,
	        	passwordValues
	        );
	        
	        if (newPasswordId == -1) {
	        	Log.e(Constants.TAG_APPLICATION_LOG, "Error on insert a new password entry in the database");
            	throw new SQLException();
			}
	        
	        /*
	    	 * Insert the related recoveries entries
	    	 */
	        if (pwd.getRecoveryList() != null && pwd.getRecoveryList().size() > 0) {
	        	RecoveryDao.getInstance(mContext).open(database);
	        	List<Long> newRecoveryList = RecoveryDao.getInstance(mContext).insertRecoveries(pwd.getRecoveryList(), newPasswordId);
	        	RecoveryDao.getInstance(mContext).close();
	 	        
	 	        if (newRecoveryList == null) {
	 	        	throw new SQLException();
	 	        }
	        }
	       
	    	
	    	// Close the transaction
	    	database.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return -1;
    	} finally {
    		database.endTransaction();
//    		Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in PasswordDao class");
    	}
    	
    	return newPasswordId;
    	
    }

    /**
     * Delete the password by id
     * 
     * @param pwdId The id of the password to delete
     */
    public void deletePassword(long pwdId) {
    	
        String whereClause = PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] whereArgs = { String.valueOf(pwdId) };
        
        database.delete(PassboxContract.PasswordEntry.TABLE_NAME, whereClause, whereArgs);
    	
    }
    
    /**
     * Delete all the password stored in the database
     * and obviously also the recoveries
     */
    public void deleteAllPasswordsAndRecoveries() {
    	
    	database.delete(PassboxContract.PasswordEntry.TABLE_NAME, null, null);
        // Obviously delete also the recovery table
        database.delete(PassboxContract.RecoveryEntry.TABLE_NAME, null, null);
        
    }
    
    /**
     * Get a password object by id
     * with only the id and the title
     * 
     * @param pwdId The id of the password to get
     * @return The light reference of the selected password
     */
    public PasswordDto getPasswordLite(long pwdId) {
    	
    	String whereClause = PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] whereArgs = { String.valueOf(pwdId) };
        
        // Get the password
        Cursor cursor = database.query(
    		PassboxContract.PasswordEntry.TABLE_NAME,	// The table to query
    		liteColumns,                             	// The columns to return
            whereClause,                             	// The columns for the WHERE clause
            whereArgs,                               	// The values for the WHERE clause
            null,                                   	// Don't group the rows
            null,                                   	// Don't filter by row groups
            null                                    	// The sort order
        );
        
        if (cursor.getCount() == 1) {
        	cursor.moveToFirst();
            PasswordDto result = cursorToPasswordLite(cursor);
        	cursor.close();
        	return result;
        } else {
        	Log.e(Constants.TAG_APPLICATION_LOG, "Founded multiple entries with the same password id");
        	cursor.close();
        	return null;
        }
        
    }
    
    /**
     * Get a password object by id
     * 
     * @param pwdId The id of the password to get
     * @return The password object selected from the database
     */
    public PasswordDto getPassword(long pwdId) {
    	
    	PasswordDto result = new PasswordDto();
    	
    	// Execute all the select queries in a transaction
    	database.beginTransaction();
//    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in PasswordDao class");
    	
    	try {
    		String whereClause = PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID + " = ?";
            String[] whereArgs = { String.valueOf(pwdId) };
            
            // Get the password
            Cursor cursor = database.query(
        		PassboxContract.PasswordEntry.TABLE_NAME,	// The table to query
                allColumns,                             	// The columns to return
                whereClause,                             	// The columns for the WHERE clause
                whereArgs,                               	// The values for the WHERE clause
                null,                                   	// Don't group the rows
                null,                                   	// Don't filter by row groups
                null                                    	// The sort order
            );
            
            if (cursor.getCount() == 1) {
            	cursor.moveToFirst();
            	result = cursorToPassword(cursor);
            	cursor.close();
            } else {
            	cursor.close();
            	Log.e(Constants.TAG_APPLICATION_LOG, "Founded multiple entries with the same password id");
            	throw new SQLException();
            }
            
            // Get the related recovery list
            RecoveryDao.getInstance(mContext).open(database);
            LinkedList<RecoveryDto> recoveryList = RecoveryDao.getInstance(mContext).getRecoveryListOfPassword(pwdId);
            RecoveryDao.getInstance(mContext).close();
            
            if (recoveryList != null && recoveryList.size() > 0) {
            	result.setRecoveryList(recoveryList);
            }
    		
    		// Close the transaction
	    	database.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			database.endTransaction();
//			Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in PasswordDao class");
		}
    	
    	return result;
    	
    }
    
    /**
     * Get all the password stored in the database
     * with only the id and the title
     *  
     * @return Th list of the stored password in the database
     */
    public LinkedList<PasswordDto> getAllPasswordsLite() {
    	
    	LinkedList<PasswordDto> results = new LinkedList<>();
    		
		// Get the passwords
        Cursor cursor = database.query(
    		PassboxContract.PasswordEntry.TABLE_NAME,	// The table to query
            liteColumns,                             	// The columns to return
            null,                             			// The columns for the WHERE clause
            null,                               		// The values for the WHERE clause
            null,                                   	// Don't group the rows
            null,                                   	// Don't filter by row groups
            null                                    	// The sort order
        );
        
        if (cursor.moveToFirst()) {
        	while (!cursor.isAfterLast()) {
        		PasswordDto password = cursorToPasswordLite(cursor);
                results.add(password);
        		cursor.moveToNext();
        	}
        } else {
//        	Log.i(Constants.TAG_APPLICATION_LOG, "None password entries founded in the database");
        	return null;
        }
        cursor.close();
    	
    	return results;
    	
    }
    
    /**
     * Get all the password stored in the database
     * @return The list of the stored password in the database
     */
    public LinkedList<PasswordDto> getAllPasswords() {
    	
    	LinkedList<PasswordDto> results = new LinkedList<>();
    	
    	// Execute all the select queries in a transaction
    	database.beginTransaction();
//    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in PasswordDao class");
    	
    	try {
    		
    		// Get the passwords
            Cursor cursor = database.query(
        		PassboxContract.PasswordEntry.TABLE_NAME,	// The table to query
                allColumns,                             	// The columns to return
                null,                             			// The columns for the WHERE clause
                null,                               		// The values for the WHERE clause
                null,                                   	// Don't group the rows
                null,                                   	// Don't filter by row groups
                null                                    	// The sort order
            );
            
            if (cursor.moveToFirst()) {
            	while (!cursor.isAfterLast()) {
            		PasswordDto password = cursorToPassword(cursor);
            		
            		// Get the related recovery list
            		RecoveryDao.getInstance(mContext).open(database);
            		LinkedList<RecoveryDto> recoveryList = RecoveryDao.getInstance(mContext).getRecoveryListOfPassword(password.getId());
            		RecoveryDao.getInstance(mContext).close();
                    
                    if (recoveryList != null && recoveryList.size() > 0) {
                    	password.setRecoveryList(recoveryList);
                    }
                    
                    results.add(password);
            		cursor.moveToNext();
            	}
            } else {
//            	Log.i(Constants.TAG_APPLICATION_LOG, "None password entries founded in the database");
            	throw new SQLException();
            }
            cursor.close();
    		
    		// Close the transaction
	    	database.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			database.endTransaction();
//			Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in PasswordDao class");
		}
    	
    	return results;
    	
    }
    
    /**
     * Update a password
     * 
     * @param pwd The updated password object
     * @return The number of affected rows in the database
     */
    public int updatePassword(PasswordDto pwd) {
    	
    	int affectedRows = 0;
    	
    	// Execute all the update queries in a transaction
    	database.beginTransaction();
//    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in RecoveryDao class");
    	
    	try {
    		/*
	    	 * Update the password entry
	    	 */
    		String whereClause = PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID + " = ?";
    		String[] whereArgs = { String.valueOf(pwd.getId()) };
    		
    		ContentValues values = new ContentValues();
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_TITLE, 		pwd.getTitle());
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_DESCRIPTION, 	pwd.getDescription());
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_USERNAME, 		pwd.getUsername());
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD, 		pwd.getPassword());
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_WEB_URL, 		pwd.getWebUrl());
    		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_COLOR, 		pwd.getColor());
	    	
	    	affectedRows = database.update(
    			PassboxContract.PasswordEntry.TABLE_NAME,
    			values,
    			whereClause,
    			whereArgs
    		);
//	    	Log.i(Constants.TAG_APPLICATION_LOG, "Affected rows = "+String.valueOf(affectedRows));
    		if (affectedRows != 1) {
    			Log.i(Constants.TAG_APPLICATION_LOG, "Error on update a password entry in the database");
            	throw new SQLException();
			}
	    	
    		/*
	    	 * Update the related recoveries entries
	    	 */
	        if (pwd.getRecoveryList() != null && pwd.getRecoveryList().size() > 0) {
	        	RecoveryDao.getInstance(mContext).open(database);
	        	int affectedRecoveryRows = RecoveryDao.getInstance(mContext).updateRecoveryList(pwd.getRecoveryList(), pwd.getId());
	        	RecoveryDao.getInstance(mContext).close();
	        	
	        	if (affectedRecoveryRows == -1) {
	        		Log.e(Constants.TAG_APPLICATION_LOG, "Error on update a password related recovery list the database");
	            	throw new SQLException();
				}
	        }
    		
    		// Close the transaction
	    	database.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return -1;
    	} finally {
    		database.endTransaction();
//    		Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in RecoveryDao class");
    	}
    	
    	return affectedRows;
    }
    
    /**
     * Update the color value of a password
     * @param newColor The updated color
     * @return The number of affected rows in the database
     */
    public int updatePasswordColor(long pwdId, String newColor) {

    	/*
    	 * Update the password entry
    	 */
		String whereClause = PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID + " = ?";
		String[] whereArgs = { String.valueOf(pwdId) };
		
		ContentValues values = new ContentValues();
		values.put(PassboxContract.PasswordEntry.COLUMN_NAME_COLOR, newColor);
    	
    	int affectedRows = database.update(
			PassboxContract.PasswordEntry.TABLE_NAME,
			values,
			whereClause,
			whereArgs
		);
    		
		if (affectedRows != 1) {
			Log.e(Constants.TAG_APPLICATION_LOG, "Error on update a password entry in the database");
        	return -1;
		} else {
			return affectedRows;
		}
    	
    }
    /**
     * Parse a cursor returned from a db query
     * to a PasswordDto Object
     * 
     * @param cursor The cursor to parse
     * @return The parsed password object
     */
    private PasswordDto cursorToPassword(Cursor cursor) {

    	PasswordDto mPassword = new PasswordDto();
    	
    	try {
	    	mPassword.setId(			cursor.getLong(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID)));
	    	mPassword.setTitle(			cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_TITLE)));
	    	mPassword.setDescription(	cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_DESCRIPTION)));
	    	mPassword.setUsername(		cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_USERNAME)));
	    	mPassword.setPassword(		cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_PASSWORD)));
	    	mPassword.setWebUrl(		cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_WEB_URL)));
	    	mPassword.setColor(			cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_COLOR)));
	    	mPassword.setRecoveryList(null);

	    	return mPassword;
    	} catch (IllegalArgumentException e) {
    		Log.e(Constants.TAG_APPLICATION_LOG, "Some error occurred on retrieving a column name in the Password Table");
			e.printStackTrace();
			return null;
		}

    }
    
    /**
     * Parse a cursor returned from a db query
     * to a PasswordDto Object with only the id and the title
     * 
     * @param cursor The cursor to parse
     * @return The pasrsed password object
     */
    private PasswordDto cursorToPasswordLite(Cursor cursor) {

    	PasswordDto mPassword = new PasswordDto();
    	
    	try {
	    	mPassword.setId(cursor.getLong(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_ENTRY_ID)));
	    	mPassword.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_TITLE)));
	    	mPassword.setColor(cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.PasswordEntry.COLUMN_NAME_COLOR)));
	    	mPassword.setRecoveryList(null);

	    	return mPassword;
    	} catch (IllegalArgumentException e) {
    		Log.e(Constants.TAG_APPLICATION_LOG, "Some error occurred on retrieving a column name in the Password Table");
			e.printStackTrace();
			return null;
		}

    }
    
}
