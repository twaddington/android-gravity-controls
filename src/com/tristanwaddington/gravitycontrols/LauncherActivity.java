package com.tristanwaddington.gravitycontrols;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tristanwaddington.gravitycontrols.service.GravityControlsService;

public class LauncherActivity extends Activity implements OnClickListener {
    private final String TAG = "LauncherActivity";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button startButton = (Button) findViewById(R.id.start_service);
        startButton.setOnClickListener(this);
        
        Button stopButton = (Button) findViewById(R.id.stop_service);
        stopButton.setOnClickListener(this);
        
        Button pauseButton = (Button) findViewById(R.id.pause);
        pauseButton.setEnabled(false);
        pauseButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onClick(View v) {
        final Intent intent = new Intent(this, GravityControlsService.class);
        switch (v.getId()) {
        case R.id.start_service:
            Log.d(TAG, "Start button!");
            startService(intent);
            break;
        case R.id.stop_service:
            Log.d(TAG, "Stop button!");
            stopService(intent);
            break;
        case R.id.pause:
            Log.d(TAG, "Pause Music");
            break;
        default:
            Log.d(TAG, "Button not implemented yet...");
        }
    }

    /** Stub */
    private boolean isServiceRunning() {
        final ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(250);
        for (int i=0; i < rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.d(TAG, rsi.service.getClassName());
            Log.d(TAG, GravityControlsService.class.getName());
            if (GravityControlsService.class.getName().equals(rsi.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}