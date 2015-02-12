package com.mcdev.passbox.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mcdev.passbox.R;

public class AboutFragment extends PreferenceFragment {

    // Constructor
    public AboutFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the preferences layout
        addPreferencesFromResource(R.xml.about);

    }
}
