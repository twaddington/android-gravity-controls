package com.tristanwaddington.gravitycontrols;

import java.util.List;

import com.android.music.IMediaPlaybackService;
import com.twaddington.android.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity extends Activity implements OnClickListener {
    private final String TAG = "LauncherActivity";
    
    private Button startButton;
    private Button stopButton;
    private Button pauseButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        startButton = (Button) findViewById(R.id.start_service);
        startButton.setOnClickListener(this);
        
        stopButton = (Button) findViewById(R.id.stop_service);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(this);
        
        pauseButton = (Button) findViewById(R.id.pause);
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
        switch (v.getId()) {
        case R.id.start_service:
            Log.d(TAG, "Start Service");
            break;
        case R.id.stop_service:
            Log.d(TAG, "Stop Service");
            break;
        case R.id.pause:
            Log.d(TAG, "Pause");
            break;
        default:
            Log.d(TAG, "Button not implemented yet...");
        }
    }
    
    private void listServices() {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        for (int i=0; i<rs.size(); i++) {
            ActivityManager.RunningServiceInfo rsi = rs.get(i);
            Log.i(TAG, "Process " + rsi.process + " with component " + rsi.service.getClassName());
        }
    }
    
    private void bindMusicPlayer() {
        Intent intent = new Intent();
        intent.setClassName("com.android.music", "com.android.music.MediaPlaybackService");
        bindService(intent, mConnection, 0);
    }
    
    private void playPause() {
        try {
            boolean isPlaying = mService.isPlaying();
            Log.d(TAG, "Is playing: "+isPlaying);
            
            if (isPlaying) {
                mService.pause();
                pauseButton.setText("Play");
            }
            else {
                mService.play();
                pauseButton.setText("Pause");
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMediaPlaybackService.Stub.asInterface(service);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);

            /*
            try {
                //mService.registerCallback(mCallback);
                
                //Log.d("MediaPlayerServiceConnection", "Playing track: " + mService.getTrackName());
                //Log.d("MediaPlayerServiceConnection", "By artist: " + mService.getArtistName());
                //if (mService.isPlaying()) {
                //    Log.d("MediaPlayerServiceConnection", "Music player is playing.");
                //} else {
                //    Log.d("MediaPlayerServiceConnection", "Music player is not playing.");
                //}
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
                e.printStackTrace();
            }
            */

            // As part of the sample, tell the user what happened.
            Log.d(TAG, "Connected to MediaPlayer service");
            Log.d("MediaPlayerServiceConnection", "Connected! Name: " + name.getClassName());
        }

        public void onServiceDisconnected(ComponentName name) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            stopButton.setEnabled(false);
            pauseButton.setEnabled(false);

            // As part of the sample, tell the user what happened.
            Log.d(TAG, "Disconnected from MediaPlayer service");
        }
    };
}