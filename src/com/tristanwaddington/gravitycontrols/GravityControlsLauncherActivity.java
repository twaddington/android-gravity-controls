package com.tristanwaddington.gravitycontrols;

import android.app.Activity;
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
}