package com.mcdev.passbox.utils.colorpicker;

import java.util.HashMap;
import java.util.Map;

import com.mcdev.passbox.R;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;

public class Utils {

	/**
	 * Utility class for colors
	 */
	public static class ColorUtils {
		
		public static final String KEY_COLOR_PRIMARY = "color_primary";
		public static final String KEY_COLOR_PRIMARY_DARK = "color_primary_dark";
		public static final String KEY_COLOR_ACCENT = "color_accent";
		public static final String KEY_COLOR_THEME = "color_theme";
		public static final int KEY_THEME_DARK = 1;
		public static final int KEY_THEME_LIGHT = 0;
		
		/**
		 * Create an array of int with colors
		 * 
		 * @param context
		 * @return
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
		 * Parse whiteColor
		 * 
		 * @return
		 */
		public static int parseWhiteColor() {
			return Color.parseColor("#FFFFFF");
		}
		
		/**
		 * From int to String hex code
		 * 
		 * @param color
		 * @return
		 */
		public static String toString(int color) {
			return String.format("#%06X", (0xFFFFFF & color));
		}
		
		/**
		 * Get the color set
		 * 
		 * @param color
		 * @return
		 */
		public static Map<String, Integer> getColorSet(Resources res, String color) {
			int intColor = Color.parseColor(color);
			Map<String, Integer> result = new HashMap<String, Integer>();
			if (intColor == res.getColor(R.color.red_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.red_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.pink_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.pink_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.purple_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.purple_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.deep_purple_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.deep_purple_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.indigo_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.indigo_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.pink_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.blue_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.blue_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.red_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_blue_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_blue_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.cyan_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.cyan_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.teal_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.teal_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.green_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.green_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_green_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_green_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.lime_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.lime_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.red_a200));
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
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.red_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.orange_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.orange_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.red_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.deep_orange_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.deep_orange_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.grey_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.grey_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.deep_orange_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else if (intColor == res.getColor(R.color.light_grey_500)) {
				result.put(KEY_COLOR_PRIMARY, intColor);
				result.put(KEY_COLOR_PRIMARY_DARK, res.getColor(R.color.light_grey_700));
				result.put(KEY_COLOR_ACCENT, res.getColor(R.color.amber_a200));
				result.put(KEY_COLOR_THEME, KEY_THEME_LIGHT);
				return result;
			} else {
				return null;
			}
			
		}
		
	}

	/**
	 * Get the size of the screen
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		int sizeScreen = context.getResources().getConfiguration().screenLayout;
		int sizeMask = Configuration.SCREENLAYOUT_SIZE_MASK;
		return (sizeScreen & sizeMask) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
}
