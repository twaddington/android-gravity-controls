package com.tristanwaddington.gravitycontrols;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {
    public static final String TAG = "SettingsActivity";
    public static final String START_ON_BOOT_KEY = "com.tristanwaddington.gravitycontrols.START_ON_BOOT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
