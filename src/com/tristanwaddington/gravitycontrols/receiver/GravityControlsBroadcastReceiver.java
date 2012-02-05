package com.tristanwaddington.gravitycontrols.receiver;

import com.tristanwaddington.gravitycontrols.SettingsActivity;
import com.tristanwaddington.gravitycontrols.service.GravityControlsService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GravityControlsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String action = intent.getAction();
        
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            // Device has finished booting, check to see if we should start
            // up the background service.
            if (preferences.getBoolean(SettingsActivity.START_ON_BOOT_KEY, false)) {
                // Start up the background service
                Intent serviceIntent = new Intent(context, GravityControlsService.class);
                context.startService(serviceIntent);
            }
        }
    }
}
