package com.tristanwaddington.gravitycontrols.service;

import com.tristanwaddington.gravitycontrols.GravityControlsLauncherActivity;
import com.tristanwaddington.gravitycontrols.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * A service class for monitoring the device sensors
 * and controlling the music stream depending on the current
 * orientation of the device.
 * 
 * @author Tristan Waddington <tristan.waddington@gmail.com>
 */
public class GravityControlsService extends Service implements SensorEventListener {
    public static final String TAG = "GravityControlsService";

    public static final int NOTIFICATION_ID = 1;

    public static final float DEVICE_MIN_FACE_UP = 8.5f;
    public static final float DEVICE_MIN_FACE_DOWN = -8.5f;
    public static final float DEVICE_MIN_TILT_LEFT = 6.5f;
    public static final float DEVICE_MIN_TILT_RIGHT = -6.5f;

    /** Binder given to clients of this service. */
    private final IBinder mBinder = new GravityControlsServiceBinder();

    /** True if a mute request was recently sent. */
    private boolean mStreamMuteRequestSent = false;

    /** True if an unmute request was recently sent. */
    private boolean mStreamUnmuteRequestSent = false;

    private AudioManager mAudioManager;
    private SensorManager mSensorManager;

    /**
     * Class used for the client Binder. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class GravityControlsServiceBinder extends Binder {
        public GravityControlsService getService() {
            // Return this instance of this service so clients can call public methods.
            return GravityControlsService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Get our service managers
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        handleCommand(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service received start command!");

        // Handle the intent
        handleCommand(intent);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Binding to service...");

        // Handle the intent
        handleCommand(intent);

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Unregister our listeners
        mSensorManager.unregisterListener(this);
    }

    /**
     * Handle an Intent given to this Service.
     * @param intnet
     */
    private void handleCommand(Intent intent) {
        final Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        
        final Intent contentIntent = new Intent(this, GravityControlsLauncherActivity.class);
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, contentIntent, 0);
        
        // Start the service in the foreground
        final Notification notification = new Notification();
        // TODO: Create a notification icon!
        notification.icon = R.drawable.icon;
        notification.when = System.currentTimeMillis();
        notification.setLatestEventInfo(this, getString(R.string.foreground_service_title),
                        getString(R.string.foreground_service_text), pendingIntent);
        startForeground(NOTIFICATION_ID, notification);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Pass
    }

    public void onSensorChanged(SensorEvent event) {
        float valueX = event.values[0];
        float valueY = event.values[1];
        float valueZ = event.values[2];
        
        if (valueZ > DEVICE_MIN_FACE_UP) {
            if (mAudioManager.isMusicActive()) {
                // Unmute the music stream!
                setMute(false);
            }
        }
        if (valueZ < DEVICE_MIN_FACE_DOWN) {
            if (mAudioManager.isMusicActive()) {
                // Mute the music stream!
                setMute(true);
            }
        }
        if (valueX > DEVICE_MIN_TILT_LEFT) {
            // Coming soon...
        }
        if (valueX < DEVICE_MIN_TILT_RIGHT) {
            // Coming soon...
        }
    }
    
    /**
     * Mute the {@link AudioManager#STREAM_MUSIC} stream. Rate limit
     * the mute requests so we don't end up with stacked mute/unmute requests.
     * 
     * @param mute
     */
    public void setMute(boolean mute) {
        if (mute && !mStreamMuteRequestSent) {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            mStreamMuteRequestSent = true;
            mStreamUnmuteRequestSent = false;
        }
        if (!mute && !mStreamUnmuteRequestSent) {
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mStreamMuteRequestSent = false;
            mStreamUnmuteRequestSent = true;
        }
    }
}
