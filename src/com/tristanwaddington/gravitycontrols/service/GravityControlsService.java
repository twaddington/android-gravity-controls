package com.tristanwaddington.gravitycontrols.service;

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

/** Stub */
public class GravityControlsService extends Service implements SensorEventListener {
    public static final String TAG = "GravityControlsService";

    public static final float DEVICE_MIN_FACE_UP = 8.5f;
    public static final float DEVICE_MIN_FACE_DOWN = -8.5f;
    public static final float DEVICE_MIN_TILT_LEFT = 6.5f;
    public static final float DEVICE_MIN_TILT_RIGHT = -6.5f;
    public static final float DEVICE_MIN_ERROR = 2.5f;

    /** Binder given to clients of this service. */
    private final IBinder mBinder = new GravityControlsServiceBinder();

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
        //String action = intent.getAction();
        
        final Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Pass
    }

    public void onSensorChanged(SensorEvent event) {
        float valueX = event.values[0];
        float valueY = event.values[1];
        float valueZ = event.values[2];
        
        Log.d(TAG, String.format("X: %s Y: %s Z: %s", valueX, valueY, valueZ));
        
        if (valueZ > DEVICE_MIN_FACE_UP) {
            Log.d(TAG, "Face up!");
            if (mAudioManager.isMusicActive()) {
                // Unmute the music stream!
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            }
        }
        if (valueZ < DEVICE_MIN_FACE_DOWN) {
            Log.d(TAG, "Face down!");
            if (mAudioManager.isMusicActive()) {
                // Mute the music stream!
                mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }
        if (valueX > DEVICE_MIN_TILT_LEFT && ((valueY + valueZ) > DEVICE_MIN_ERROR)) {
            Log.d(TAG, "Left tilt!");
        }
        if (valueX < DEVICE_MIN_TILT_RIGHT && ((valueY + valueZ) > DEVICE_MIN_ERROR)) {
            Log.d(TAG, "Right tilt!");
        }
    }
}
