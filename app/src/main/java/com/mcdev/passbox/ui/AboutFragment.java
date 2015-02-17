package com.mcdev.passbox.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
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

        Context context = getActivity().getApplicationContext();
        String appVersion;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }

        Preference version = findPreference("pref_version");
        version.setSummary(appVersion);

    }
}
