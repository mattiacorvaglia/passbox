package com.mcdev.passbox.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mcdev.passbox.R;

public class SettingsFragment extends PreferenceFragment {
	
	// Constructor
	public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the preferences layout
        addPreferencesFromResource(R.xml.preferences);

    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }*/
    
}
