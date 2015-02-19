package com.mcdev.passbox.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Util;

public class AboutFragment extends PreferenceFragment {

    // Constructor
    public AboutFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add the preferences layout
        addPreferencesFromResource(R.xml.about);

        final Context context = getActivity().getApplicationContext();
        String appVersion;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }

        /*
         Show the application version
         */
        Preference version = findPreference("pref_version");
        version.setSummary(appVersion);

        /*
         Send a bug report via email
         */
        Preference bugReport = findPreference("pref_bug");
        bugReport.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL,   new String[] { "corvagliamattia@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "PASSBOX - BUG REPORT");
                intent.putExtra(Intent.EXTRA_TEXT,    "");
                getActivity().startActivity(Intent.createChooser(intent, getString(R.string.action_choose_email_client)));
                return true;
            }
        });

    }
}
