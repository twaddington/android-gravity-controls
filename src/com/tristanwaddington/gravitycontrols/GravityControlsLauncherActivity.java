package com.tristanwaddington.gravitycontrols;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tristanwaddington.gravitycontrols.service.GravityControlsService;

public class GravityControlsLauncherActivity extends Activity implements OnClickListener {
    private final String TAG = "GravityControlsLauncherActivity";
    
    private Button mStartButton;
    private Button mStopButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mStartButton = (Button) findViewById(R.id.start_service);
        mStartButton.setOnClickListener(this);
        
        mStopButton = (Button) findViewById(R.id.stop_service);
        mStopButton.setOnClickListener(this);
        
        // Update the button state
        setButtonState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.preferences:
            Intent intent = new Intent(this, GravityControlsSettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        final Intent intent = new Intent(this, GravityControlsService.class);
        switch (v.getId()) {
        case R.id.start_service:
            Log.d(TAG, "Start button!");
            startService(intent);
            
            // Update the button state
            setButtonState();
            break;
        case R.id.stop_service:
            Log.d(TAG, "Stop button!");
            stopService(intent);
            
            // Update the button state
            setButtonState();
            break;
        default:
            Log.d(TAG, "Button not implemented yet...");
        }
    }

    /**
     * Determine if the background service is running.
     * @return true if the service is running, false if otherwise.
     */
    public boolean isServiceRunning() {
        final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(250);
        
        for (ActivityManager.RunningServiceInfo s : services) {
            Log.d(TAG, s.service.getClassName());
            if (GravityControlsService.class.getName().equals(s.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Toggle the visibility of the start/stop service buttons
     * depending on the state of the background service.
     */
    public void setButtonState() {
        if (isServiceRunning()) {
            mStartButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.VISIBLE);
        } else {
            mStartButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.GONE);
        }
    }
}