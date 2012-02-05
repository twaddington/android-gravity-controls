package com.tristanwaddington.gravitycontrols.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/** Stub */
public class GravityControlsService extends Service implements SensorEventListener {
    public static final String TAG = "GravityControlsService";

    /** Binder given to clients of this service. */
    private final IBinder mBinder = new GravityControlsServiceBinder();

    /** Stub */
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
        
        // Get our sensor manager
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
        String action = intent.getAction();
        
        final Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, String.format("onAccuracyChanged for sensor %s (%d)",
                        sensor.getName(), accuracy));
    }

    public void onSensorChanged(SensorEvent event) {
        // UP:   Z +9
        // DOWN: Z -9
        // LEFT TILT: X+9
        // RIGHT TILT: X-9
        
        //Log.d(TAG, String.format("onSensorChanged for sensor %s",
        //                event.sensor.getName()));
        //Log.d(TAG, "SensorEvent: "+event);
        //Log.d(TAG, "Values: "+event.values);
        Log.d(TAG, String.format("X: %s, Y: %s, Z: %s",
                        event.values[0], event.values[1], event.values[2]));
    }
}
