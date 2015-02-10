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

public class RecoveryDao {
	
	private SQLiteDatabase database;
	private PassboxDbHelper mDbHelper;
    private String[] allColumns = {
		PassboxContract.RecoveryEntry.COLUMN_NAME_ENTRY_ID,
		PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION,
		PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER,
		PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY,
    };
    
    // Singleton
    private static RecoveryDao instance = null;
	
	// Constructor
	protected RecoveryDao(Context context) {
		mDbHelper = new PassboxDbHelper(context);
		Log.i(Constants.TAG_APPLICATION_LOG, "new PassboxDbHelper operation executed in RecoveryDao class");
	}
	
	// Singleton Pattern
	public static RecoveryDao getInstance(Context context) {
		if (instance == null) {
			instance = new RecoveryDao(context);
		}
		return instance;
	}
	
	/**
	 * Open DB connection
	 * 
	 * @throws SQLException
	 */
    public void open(Context context) throws SQLException {
    	mDbHelper = new PassboxDbHelper(context);
    	database = mDbHelper.getWritableDatabase();
        Log.i(Constants.TAG_APPLICATION_LOG, "getWritableDatabase() operation executed in RecoveryDao class");
    }
	
	/**
	 * Open DB connection
	 * 
	 * @throws SQLException
	 */
    public void open(SQLiteDatabase database) throws SQLException {
        this.database = database;
        Log.i(Constants.TAG_APPLICATION_LOG, "Set database with the existing one in RecoveryDao class");
    }

    /**
     * Close DB connection
     */
    public void close() {
        mDbHelper.close();
        Log.i(Constants.TAG_APPLICATION_LOG, "database close() operation executed in PasswordDao class");
    }
	
    /**
     * Insert a number of recovery entries in the database
     * with their parent password reference
     * 
     * @param rcvs The List of recovery
     * @param pwdId The parent password id
     * @return
     */
    public List<Long> insertRecoveries(List<RecoveryDto> rcvs, long pwdId) {
    	
    	List<Long> results = new LinkedList<Long>();
    	
    	// Execute all the insert queries in a transaction
    	database.beginTransaction();
    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in RecoveryDao class");
    	
    	try {
    		// Iterate each recovery to store in the database
	    	for (RecoveryDto rcv : rcvs) {
	    		ContentValues values = new ContentValues();
	    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION, 		rcv.getQuestion());
	    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER, 		rcv.getAnswer());
	    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY, 	pwdId);
	    		
	    		// Insert the new row, returning the primary key value of the new row
	            long newRecoveryId = database.insert(PassboxContract.RecoveryEntry.TABLE_NAME, PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY, values);
	            
	            if (newRecoveryId == -1) {
	            	throw new SQLException();
				} else {
					results.add(newRecoveryId);
				}
	    	}
	    	
	    	if (rcvs.size() != results.size()) {
        		Log.i(Constants.TAG_APPLICATION_LOG, "Some recovery entries was not succesfully stored in the database");
        		throw new SQLException();
        	}
	    	
	    	// Close the transaction
	    	database.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return null;
    	} finally {
    		database.endTransaction();
    		Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in RecoveryDao class");
    	}
    	
    	return results;
    	
    }
	
    /**
     * Get the list of recovery entries of the password
     * 
     * @param pwdId
     * @return
     */
    public LinkedList<RecoveryDto> getRecoveryListOfPassword(long pwdId) {
    	
    	LinkedList<RecoveryDto> results = new LinkedList<RecoveryDto>();
    	
    	String whereClause = PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY + " = ?";
        String[] whereArgs = { String.valueOf(pwdId) };
        
        Cursor cursor = database.query(
    		PassboxContract.RecoveryEntry.TABLE_NAME,	// The table to query
            allColumns,                             	// The columns to return
            whereClause,                            	// The columns for the WHERE clause
            whereArgs,                               	// The values for the WHERE clause
            null,                                   	// Don't group the rows
            null,                                   	// Don't filter by row groups
            null                                    	// The sort order
        );
        
        if (cursor.moveToFirst()) {
        	while (!cursor.isAfterLast()) {
        		RecoveryDto recovery = cursorToRecovery(cursor);
        		results.add(recovery);
        		cursor.moveToNext();
        	}
        } else {
        	Log.i(Constants.TAG_APPLICATION_LOG, "None recovery entries founded for this password");
        }
        cursor.close();
        
        return results;
    	
    }
    
    /**
     * Update the list of recovery
     * 
     * @param rcvs
     * @param pwdId
     * @return
     */
    public int updateRecoveryList(List<RecoveryDto> rcvs, long pwdId) {
    	
    	int affectedRows = 0;
    	
    	// Execute all the update queries in a transaction
    	database.beginTransaction();
    	Log.i(Constants.TAG_APPLICATION_LOG, "beginTransaction() executed in RecoveryDao class");
    	
    	try {
    		// Iterate each recovery to update in the database
	    	for (RecoveryDto rcv : rcvs) {
	    		
	    		// Check if just existing or new
	    		if (rcv.getId() == 0) {
	    			/*
	    			 * It's a new recovery entry
	    			 */
	    			ContentValues values = new ContentValues();
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION, 		rcv.getQuestion());
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER, 		rcv.getAnswer());
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY, 	pwdId);
		    		
		    		// Insert the new row, returning the primary key value of the new row
		            long newRecoveryId = database.insert(PassboxContract.RecoveryEntry.TABLE_NAME, PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY, values);
		            
		            if (newRecoveryId == -1) {
		            	Log.i(Constants.TAG_APPLICATION_LOG, "Error on inserting a new recovery entry in the database");
		            	throw new SQLException();
					} else {
						affectedRows++;
					}
	    		} else {
	    			/*
	    			 * It's an existing recovery entry
	    			 */
		    		String whereClause = PassboxContract.RecoveryEntry.COLUMN_NAME_ENTRY_ID + " = ?";
		    		String[] whereArgs = { String.valueOf(rcv.getId()) };
		    		
		    		ContentValues values = new ContentValues();
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION, 		rcv.getQuestion());
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER, 		rcv.getAnswer());
		    		values.put(PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY, 	rcv.getPasswordKey());
		    		
		    		int affectedRowsTemp = database.update(
		    			PassboxContract.RecoveryEntry.TABLE_NAME,
		    			values,
		    			whereClause,
		    			whereArgs
		    		);
		    		
		    		if (affectedRowsTemp == 0) {
		    			Log.i(Constants.TAG_APPLICATION_LOG, "Error on updating an existing recovery entry in the database");
		            	throw new SQLException();
					} else {
						affectedRows++;
					}
	    		}
	    		
	    	}
	    	
	    	if (affectedRows != rcvs.size()) {
	    		Log.i(Constants.TAG_APPLICATION_LOG, "Some recovery entries are not been updated in the database");
	    		throw new SQLException();
	    	}
	    	
	    	// Close the transaction
	    	database.setTransactionSuccessful();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return -1;
    	} finally {
    		database.endTransaction();
    		Log.i(Constants.TAG_APPLICATION_LOG, "endTransaction() executed in RecoveryDao class");
    	}
    	
    	return affectedRows;
    	
    }
    
    /**
     * Delete the recovery by id
     * 
     * @param rcvId
     */
    public void deleteRecovery(long rcvId) {
    	
        String whereClause = PassboxContract.RecoveryEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] whereArgs = { String.valueOf(rcvId) };
        
        database.delete(PassboxContract.RecoveryEntry.TABLE_NAME, whereClause, whereArgs);
    	
    }
	
	
	/**
     * Parse a cursor returned from a db query
     * to a RecoveryDto Object
     * 
     * @param cursor
     * @return
     */
    private RecoveryDto cursorToRecovery(Cursor cursor) {
    	
    	RecoveryDto mRecovery = new RecoveryDto();
    	
    	try {
        	mRecovery.setId(			cursor.getLong(cursor.getColumnIndexOrThrow(PassboxContract.RecoveryEntry.COLUMN_NAME_ENTRY_ID)));
        	mRecovery.setPasswordKey(	cursor.getLong(cursor.getColumnIndexOrThrow(PassboxContract.RecoveryEntry.COLUMN_NAME_PASSWORD_KEY)));
        	mRecovery.setQuestion(		cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.RecoveryEntry.COLUMN_NAME_QUESTION)));
        	mRecovery.setAnswer(		cursor.getString(cursor.getColumnIndexOrThrow(PassboxContract.RecoveryEntry.COLUMN_NAME_ANSWER)));
        	
        	return mRecovery;
		} catch (IllegalArgumentException e) {
			Log.i(Constants.TAG_APPLICATION_LOG, "Some error occurred on retrieving a column name in the Recovery Table");
			e.printStackTrace();
			return null;
		}
    	
    }

}
