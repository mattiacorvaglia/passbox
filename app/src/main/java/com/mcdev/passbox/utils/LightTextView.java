package com.mcdev.passbox.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LightTextView extends TextView {

	/*
	 * Constructors
	 */
	public LightTextView(Context context) {
		super(context);
		createFont();
	}

	public LightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		createFont();
	}

	public LightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		createFont();
	}

	/**
	 * Set the light Typeface
	 */
	private void createFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Light.ttf");
//		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Thin.ttf");
		setTypeface(font);
	}


}
