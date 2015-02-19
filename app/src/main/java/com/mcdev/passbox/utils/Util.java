package com.mcdev.passbox.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PassboxDbHelper;
import com.mcdev.passbox.views.FilePicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Util {

	/**
	 * Utility class for colors
	 */
	public static class Colors {
		
		public static final String KEY_COLOR_PRIMARY        = "color_primary";
		public static final String KEY_COLOR_PRIMARY_DARK   = "color_primary_dark";
		public static final String KEY_COLOR_ACCENT         = "color_accent";
		public static final String KEY_COLOR_THEME          = "color_theme";
		public static final int KEY_THEME_DARK  = 1;
		public static final int KEY_THEME_LIGHT = 0;
		
		/**
		 * Create an array of int with colors
		 * 
		 * @param context The context of the calling Activity
		 * @return The list of the colors to choice
		 */
		public static int[] colorChoice(Context context) {

			int[] mColorChoices = null;	
			String[] color_array = context.getResources().getStringArray(R.array.default_color_choice_values);

			if (color_array != null && color_array.length > 0) {
				mColorChoices = new int[color_array.length];
				for (int i = 0; i < color_array.length; i++) {
					mColorChoices[i] = Color.parseColor(color_array[i]);
				}
			}
			return mColorChoices;
		}
		
		/**
		 * From int to String hex code
		 * 
		 * @param color The Integer code of the color
		 * @return The hex code of the color
		 */
		public static String toString(int color) {
			return String.format("#%06X", (0xFFFFFF & color));
		}

        /**
         * Get the color set
         * @param res The Resources reference
         * @param color The main color
         * @return A Map with the color set
         */
		public static Map<String, Integer> getColorSet(Resources res, String color) {
			int intColor = Color.parseColor(color);
			Map<String, Integer> result = new HashMap<>();
			if (intColor == res.getColor(R.color.red_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.red_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.pink_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.pink_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.purple_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.purple_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.deep_purple_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.deep_purple_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.indigo_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.indigo_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.blue_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.blue_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_blue_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_blue_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.cyan_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.cyan_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.teal_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.teal_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.green_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.green_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_green_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_green_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.lime_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.lime_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.yellow_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.yellow_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.orange_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.amber_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.amber_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.red_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.orange_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.orange_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.deep_orange_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.deep_orange_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.grey_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.grey_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_grey_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_grey_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_500));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else {
				return null;
			}
			
		}
		
	}

    /**
     * Strings util class
     */
    public static class Strings {

        /**
         * Capitalize a String
         *
         * @param string The String to capitalize
         * @return The capitalized String
         */
        public static String capitalize(String string) {
            if (string == null || string.length() == 0) {
                return "";
            }
            char first = string.charAt(0);
            if (Character.isUpperCase(first)) {
                return string;
            } else {
                return Character.toUpperCase(first) + string.substring(1);
            }
        }

        /**
         * Check if a String object is a NULL object
         * or a "null" String or an empty String
         *
         * @param string The String object to check up
         * @return true if the String is NULL or empty
         */
        public static boolean isNullOrEmpty(@Nullable String string) {
            return string == null || string.length() < 1;
        }

        /**
         * Encrypt password with md5 digest
         * @param pwd The String to encrypt
         * @return The encrypted String
         */
        public static String toMd5(String pwd) {

            try {

                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(pwd.getBytes());

                byte[] digest = messageDigest.digest();

                BigInteger bigInt = new BigInteger(1, digest);

                String hashText = bigInt.toString(16);

                // Now we need to zero pad it if you actually want the full 32 chars.
                while(hashText.length() < 32 ) {
                    hashText = "0"+hashText;
                }

                return hashText;

            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return pwd;
            }

        }

    }

    /**
     * Util class for cryptography
     */
    public static class Crypto {

        private static final String CIPHER_ALGORITHM                = "AES";
        private static final String CIPHER_ALGORITHM_MODE_PADDING   = "AES/CBC/PKCS5Padding";
        private static final String PBKDF2_DERIVATION_ALGORITHM     = "PBKDF2WithHmacSHA1";
        private static final String PBKDF28BIT_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1And8bit";
        private static final String DELIMITER                       = "]";
        private static final String CHARSET                         = "UTF-8";
        // The size of the salt should typically match the key size
        private static final int SALT_LENGTH                        = 256;  // bits
        private static final int KEY_LENGTH                         = 256;  // bits
        private static final int IV_LENGTH                          = 128;  // bits
        /*
         * Number of PBKDF2 hardening rounds to use. Larger values increase
         * computation time. You should select a value that causes computation
         * to take >100ms.
         * Minimum value recommended by PKCS#5 is 1000
        */
        private static final int ITERATIONS                         = 3000;

        /**
         * Generate the salt to use to generate the key
         * @return The generated salt
         * @throws NoSuchAlgorithmException
         */
        private static SecretKey generateSalt() throws NoSuchAlgorithmException {

            SecureRandom secureRandom = new SecureRandom();
            // Do *not* seed secureRandom! Automatically seeded from system entropy.
            KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER_ALGORITHM);
            keyGenerator.init(SALT_LENGTH, secureRandom);
            return keyGenerator.generateKey();
            
        }

        /**
         * Generate the Key from passphrase
         * @param passphraseOrPin the passphrase or the PIN to use to generate the key
         * @param salt The salt to use to generate the key
         * @return The generate key
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         * @throws UnsupportedEncodingException
         */
        private static SecretKey generateKeyFromPassphrase(String passphraseOrPin, byte[] salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException,
                UnsupportedEncodingException {

            SecretKeyFactory secretKeyFactory;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Use compatibility key factory -- only uses lower 8-bits of passphrase chars
                secretKeyFactory = SecretKeyFactory.getInstance(PBKDF28BIT_DERIVATION_ALGORITHM);
            } else {
                /*
                 * Traditional key factory. Will use lower 8-bits of passphrase chars on
                 * older Android versions (API level 18 and lower) and all available bits
                 * on KitKat and newer (API level 19 and higher).
                 */
                secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_DERIVATION_ALGORITHM);
            }
            KeySpec keySpec = new PBEKeySpec(
                    passphraseOrPin.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH);
            return secretKeyFactory.generateSecret(keySpec);
            
        }

        /**
         * Generate the Initialization Vector (IV)
         * @return the generated Initialization Vector (IV)
         * @throws NoSuchAlgorithmException
         */
        private static SecretKey generateIV() throws NoSuchAlgorithmException {

            SecureRandom secureRandom = new SecureRandom();
            // Do *not* seed secureRandom! Automatically seeded from system entropy.
            KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER_ALGORITHM);
            keyGenerator.init(IV_LENGTH, secureRandom);
            return keyGenerator.generateKey();
            
        }

        /**
         * Encode a bytes array to a Base64 String
         * @param bytes The array to encode
         * @return The Base64 encoded String
         */
        private static String toBase64(byte[] bytes) {
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        }

        /**
         * Decode a Base64 encoded String to a bytes array
         * @param string The String to decode
         * @return The decoded bytes array
         */
        private static byte[] fromBase64(String string) {
            return Base64.decode(string, Base64.NO_WRAP);
        }

        /**
         * Encrypt a plain text String
         * @param plainText The String to encrypt
         * @param passphraseOrPin The passphrase or PIN to use to generate
         *                        the encryption key
         * @return The storable encrypted String
         * @throws NoSuchAlgorithmException
         * @throws UnsupportedEncodingException
         * @throws InvalidKeySpecException
         * @throws NoSuchPaddingException
         * @throws InvalidAlgorithmParameterException
         * @throws InvalidKeyException
         * @throws BadPaddingException
         * @throws IllegalBlockSizeException
         */
        public static String encrypt(String plainText, String passphraseOrPin)
                throws NoSuchAlgorithmException, UnsupportedEncodingException,
                InvalidKeySpecException, NoSuchPaddingException,
                InvalidAlgorithmParameterException, InvalidKeyException,
                BadPaddingException, IllegalBlockSizeException {
            
            // Generate a key from the passphrase/PIN
            SecretKey salt = generateSalt();
            SecretKey key = generateKeyFromPassphrase(passphraseOrPin, salt.getEncoded());
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), CIPHER_ALGORITHM);
            // Generate an Initialization Vector (IV)
            SecretKey iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getEncoded());
            // Initialize the cipher
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_MODE_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            // Encrypt
            byte[] encrypted = cipher.doFinal(plainText.getBytes(CHARSET));
            /*
             * Build the encrypted String to store
             * format: "salt + divider + iv + divider + encrypted"
             */
            return String.format("%s%s%s%s%s", toBase64(salt.getEncoded()), DELIMITER,
                    toBase64(iv.getEncoded()), DELIMITER, toBase64(encrypted));
            
        }

        /**
         * Decrypt an encrypted String
         * @param encrypted The encrypted String
         * @param passphraseOrPin The passphrase or PIN to use to generate
         *                        the encryption key
         * @return The decrypted String
         * @throws NoSuchPaddingException
         * @throws NoSuchAlgorithmException
         * @throws InvalidKeySpecException
         * @throws UnsupportedEncodingException
         * @throws InvalidAlgorithmParameterException
         * @throws InvalidKeyException
         * @throws BadPaddingException
         * @throws IllegalBlockSizeException
         */
        public static String decrypt(String encrypted, String passphraseOrPin)
                throws NoSuchPaddingException, NoSuchAlgorithmException,
                InvalidKeySpecException, UnsupportedEncodingException,
                InvalidAlgorithmParameterException, InvalidKeyException,
                BadPaddingException, IllegalBlockSizeException {

            // Extract the encrypted data from the String
            String[] fields = encrypted.split(DELIMITER);
            if (fields.length != 3) {
                throw new IllegalArgumentException("Invalid encypted text format");
            }

            byte[] salt         = fromBase64(fields[0]);
            byte[] iv           = fromBase64(fields[1]);
            byte[] cipherBytes  = fromBase64(fields[2]);

            // Recreate the key
            SecretKey key = generateKeyFromPassphrase(passphraseOrPin, salt);

            // Recreate the specifics
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), CIPHER_ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Initialize the cipher
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_MODE_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            // Decrypt
            byte[] decrypted = cipher.doFinal(cipherBytes);
            
            return new String(decrypted, CHARSET);
        }
    }

    /**
     * Util class for handling files
     */
    public static class Files {

        private static final String PATH_DATA       = "/data/";
        private static final String APP_PACKAGE     = "com.mcdev.passbox";
        private static final String PATH_DATABASE   = "/databases/";
        private static final String PATH_BACKUP     = "/Passbox/";
        private static final String DB_EXTENSION    = ".db";

        public static final int EXPORT_COMPLETED        = 1;
        public static final int EXPORT_CANNOT_WRITE_SD  = 2;
        public static final int EXPORT_IO_EXCEPTION     = 3;
        public static final int EXPORT_EXCEPTION        = 4;
        public static final int IMPORT_COMPLETED        = 5;
        public static final int IMPORT_CANNOT_READ_SD   = 6;
        public static final int IMPORT_IO_EXCEPTION     = 7;
        public static final int IMPORT_FNF_EXCEPTION    = 8;
        public static final int IMPORT_EXCEPTION        = 9;

        /**
         * Export a backup copy of the databse in the
         * external storage of the device
         *
         * @return The result code of the operation
         */
        public static int exportDB() {

            try {

                File sd     = Environment.getExternalStorageDirectory();
                File data   = Environment.getDataDirectory();

                if (sd.canWrite()) {

                    // Create a new folder for the database to be backed up to
                    File backupFolder = new File(Environment.getExternalStorageDirectory() + PATH_BACKUP);

                    if (!backupFolder.exists()) {
                        if (!backupFolder.mkdir()) {
                            Log.e(Constants.TAG_APPLICATION_LOG, "Impossible to create the backup folder");
                        }
                    }

                    String currentDBPath    = PATH_DATA + APP_PACKAGE + PATH_DATABASE +
                                              PassboxDbHelper.DATABASE_NAME;
                    String backupDBPath     = PATH_BACKUP + PassboxDbHelper.DATABASE_NAME;

                    File currentDB  = new File(data,    currentDBPath);
                    File backupDB   = new File(sd,      backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();

                    dst.transferFrom(src, 0, src.size());

                    src.close();
                    dst.close();

                    Log.i(Constants.TAG_APPLICATION_LOG, "DB exported");
                    return EXPORT_COMPLETED;
                } else {
                    Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, cannot write the external storage");
                    return EXPORT_CANNOT_WRITE_SD;
                }

            } catch (IOException ioe) {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, IOException");
                ioe.printStackTrace();
                return EXPORT_IO_EXCEPTION;
            } catch (Exception e) {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, Exception");
                e.printStackTrace();
                return EXPORT_EXCEPTION;
            }
        }

        /**
         * Launch a dialog to pick the backup file
         * @param context The context
         */
        public static void chooseBackup(final Context context) {

            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                File mPath = new File(sd + PATH_BACKUP);
                FilePicker filePicker = new FilePicker(context, mPath);
                filePicker.setFileEndsWith(DB_EXTENSION);
                filePicker.addFileListener(new FilePicker.FileSelectedListener() {
                    public void fileSelected(File file) {
                        Log.d(Constants.TAG_APPLICATION_LOG, "Selected file: " + file.toString());
                        // Import
                        int res = importDB(file);
                        // Check the operation result
                        switch (res) {
                            case IMPORT_COMPLETED:
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.export_ok_title))
                                        .setMessage(context.getString(R.string.export_ok_message))
                                        .setPositiveButton(context.getString(R.string.the_end),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {}
                                                }
                                        ).show();
                                break;
                            case IMPORT_FNF_EXCEPTION:
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.export_ko_title))
                                        .setMessage(context.getString(R.string.export_ko_message) +
                                                context.getString(R.string.export_ko_exception_1))
                                        .setPositiveButton(context.getString(R.string.the_end),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }
                                        ).show();
                                break;
                            case IMPORT_IO_EXCEPTION:
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.export_ko_title))
                                        .setMessage(context.getString(R.string.export_ko_message) +
                                                context.getString(R.string.export_ko_exception_1))
                                        .setPositiveButton(context.getString(R.string.the_end),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }
                                        ).show();
                                break;
                            case IMPORT_EXCEPTION:
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.export_ko_title))
                                        .setMessage(context.getString(R.string.export_ko_message) +
                                                context.getString(R.string.export_ko_exception_1))
                                        .setPositiveButton(context.getString(R.string.the_end),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {}
                                                }
                                        ).show();
                                break;
                            default:
                                new AlertDialog.Builder(context)
                                        .setTitle(context.getString(R.string.export_ko_title))
                                        .setMessage(context.getString(R.string.export_ko_message) +
                                                context.getString(R.string.export_ko_exception_1))
                                        .setPositiveButton(context.getString(R.string.the_end),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                }
                                        ).show();
                                break;
                        }
                    }
                });
                filePicker.showDialog();
            } else {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, cannot read the external storage");
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.export_ko_title))
                        .setMessage(context.getString(R.string.export_ko_message) +
                                context.getString(R.string.export_ko_exception_1))
                        .setPositiveButton(context.getString(R.string.the_end),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                }
                        ).show();
            }
        }
        
        /**
         * Import the database from a backup copy stored
         * in the external storage of the device
         *
         * @param file the file to import
         * @return The result code of the operation
         */
        public static int importDB(File file) {

            try {
                
                File data   = Environment.getDataDirectory();

                String currentDBPath    = PATH_DATA + APP_PACKAGE + PATH_DATABASE +
                        PassboxDbHelper.DATABASE_NAME;
//                String backupDBPath     = PATH_BACKUP + PassboxDbHelper.DATABASE_NAME;

//                File currentDB  = new File(sd, backupDBPath);
                File backupDB   = new File(data, currentDBPath);

                FileChannel src = new FileInputStream(file).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();

                dst.transferFrom(src, 0, src.size());

                src.close();
                dst.close();

                return IMPORT_COMPLETED;

            } catch (FileNotFoundException fnfe) {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, FileNotFoundException");
                fnfe.printStackTrace();
                return IMPORT_FNF_EXCEPTION;
            } catch (IOException ioe) {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, IOException");
                ioe.printStackTrace();
                return IMPORT_IO_EXCEPTION;
            } catch (Exception e) {
                Log.e(Constants.TAG_APPLICATION_LOG, "DB not exported, Exception");
                e.printStackTrace();
                return IMPORT_EXCEPTION;
            }
        }

    }
    
	/**
	 * Get the size of the screen
	 * 
	 * @param context The context of the calling Activity
	 * @return true if the device is a tablet, else false
	 */
	public static boolean isTablet(Context context) {
		int sizeScreen = context.getResources().getConfiguration().screenLayout;
		int sizeMask = Configuration.SCREENLAYOUT_SIZE_MASK;
		return (sizeScreen & sizeMask) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
}
