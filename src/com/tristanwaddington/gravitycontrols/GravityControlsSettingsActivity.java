package com.tristanwaddington.gravitycontrols;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class GravityControlsSettingsActivity extends PreferenceActivity
        implements OnPreferenceClickListener {
    public static final String TAG = "GravityControlsSettingsActivity";
    public static final String FEEDBACK_EMAIL_ADDRESS = "tristan.waddington@gmail.com";
    public static final String FEEDBACK_EMAIL_SUBJECT = "GravityControls version %s app feedback";
    public static final String START_ON_BOOT_KEY = "com.tristanwaddington.gravitycontrols.START_ON_BOOT_KEY";
    public static final String APP_VERSION_KEY = "app_version";
    public static final String RATE_THE_APP_KEY = "rate_the_app";
    public static final String SEND_FEEDBACK_KEY = "send_feedback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference versionPreference = findPreference(APP_VERSION_KEY);
        if (versionPreference != null) {
            versionPreference.setSummary(getAppVersionFromManifest());
        }
        
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
        final Intent intent = new Intent();
        final String key = preference.getKey();
        if (RATE_THE_APP_KEY.equals(key)) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.format("market://details?id=%s",
                            getPackageName())));
            startActivity(intent);
            return true;
        } else if (SEND_FEEDBACK_KEY.equals(key)){
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {FEEDBACK_EMAIL_ADDRESS});
            intent.putExtra(Intent.EXTRA_SUBJECT, String.format(FEEDBACK_EMAIL_SUBJECT,
                            getAppVersionFromManifest()));
            intent.putExtra(Intent.EXTRA_TEXT, String.format("\n\n %s", getDeviceString()));
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Gets the app version string that was set in
     * the AndroidManifest.xml file.
     * 
     * @return The app version String from the AndroidManifest.
     */
    public String getAppVersionFromManifest() {
        PackageManager packageManager = getPackageManager();
        try {
            return packageManager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Could not get application version string from manifest", e);
        }
        
        return null;
    }

    /**
     * Returns a String describing the device hardware including the running
     * version of Android and the app build number.
     * 
     * @return A String.
     */
    public String getDeviceString() {
        return String.format("Device: Android %s %s %s %s %s",
                Build.VERSION.RELEASE, Build.BRAND, Build.MANUFACTURER, Build.MODEL, Build.DEVICE);
    }
}
