package com.tristanwaddington.gravitycontrols;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class GravityControlsSettingsActivity extends PreferenceActivity
        implements OnPreferenceClickListener {
    public static final String TAG = "GravityControlsSettingsActivity";
    public static final String FEEDBACK_EMAIL_ADDRESS = "tristan.waddington@gmail.com";
    public static final String FEEDBACK_EMAIL_SUBJECT = "GravityControls version 1.0.0 app feedback";
    public static final String START_ON_BOOT_KEY = "com.tristanwaddington.gravitycontrols.START_ON_BOOT_KEY";
    public static final String RATE_THE_APP_KEY = "rate_the_app";
    public static final String SEND_FEEDBACK_KEY = "send_feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference ratePreference = findPreference(RATE_THE_APP_KEY);
        if (ratePreference != null) {
            ratePreference.setOnPreferenceClickListener(this);
        }
        
        Preference feedbackPreference = findPreference(SEND_FEEDBACK_KEY);
        if (feedbackPreference != null) {
            feedbackPreference.setOnPreferenceClickListener(this);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String packageName = getClass().getPackage().getName();
        Log.d(TAG, packageName);
        
        final Intent intent = new Intent();
        final String key = preference.getKey();
        if (RATE_THE_APP_KEY.equals(key)) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.format("market://details?id=%s",
                            packageName)));
            startActivity(intent);
            return true;
        } else if (SEND_FEEDBACK_KEY.equals(key)){
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {FEEDBACK_EMAIL_ADDRESS});
            intent.putExtra(Intent.EXTRA_SUBJECT, FEEDBACK_EMAIL_SUBJECT);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
