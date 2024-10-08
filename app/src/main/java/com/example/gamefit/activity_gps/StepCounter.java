package com.example.gamefit.activity_gps;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

public class StepCounter implements SensorEventListener {

    private final AppCompatActivity activity;
    private final SensorManager sensorManager;
    private final Sensor linearAccelerationSensor;

    private final MutableLiveData<Integer> liveSpeed = new MutableLiveData<>();
    private long lastUpdateTime = 0;
    private float[] velocity = new float[3];

    public StepCounter(AppCompatActivity activity) {
        this.activity = activity;
        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void setupStepCounter() {
        if (linearAccelerationSensor != null) {
            sensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unloadStepCounter() {
        if (linearAccelerationSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            long currentTime = System.currentTimeMillis();
            if (lastUpdateTime == 0) {
                lastUpdateTime = currentTime;
                return;
            }

            float deltaTime = (currentTime - lastUpdateTime) / 1000.0f; // Convert ms to seconds
            lastUpdateTime = currentTime;

            // Update velocity by integrating acceleration
            for (int i = 0; i < 3; i++) {
                velocity[i] += event.values[i] * deltaTime;
            }

            // Calculate the speed (magnitude of the velocity vector)
            float speed = (float) Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1] + velocity[2] * velocity[2]);

            liveSpeed.postValue((int) speed);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed
    }

    public MutableLiveData<Integer> getLiveSpeed() {
        return liveSpeed;
    }
}
