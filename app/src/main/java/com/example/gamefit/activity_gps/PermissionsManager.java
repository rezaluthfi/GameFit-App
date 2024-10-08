package com.example.gamefit.activity_gps;

import android.Manifest;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionsManager {

    private final AppCompatActivity activity;
    private final LocationProvider locationProvider;
    private final StepCounter stepCounter;

    private final ActivityResultLauncher<String> locationPermissionProvider;
    private final ActivityResultLauncher<String> activityRecognitionPermissionProvider;

    public PermissionsManager(AppCompatActivity activity, LocationProvider locationProvider, StepCounter stepCounter) {
        this.activity = activity;
        this.locationProvider = locationProvider;
        this.stepCounter = stepCounter;

        locationPermissionProvider = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), granted -> {
                    if (granted) {
                        locationProvider.getUserLocation();
                    }
                });

        activityRecognitionPermissionProvider = activity.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), granted -> {
                    if (granted) {
                        stepCounter.setupStepCounter();
                    }
                });
    }

    public void requestUserLocation() {
        locationPermissionProvider.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public void requestActivityRecognition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityRecognitionPermissionProvider.launch(Manifest.permission.ACTIVITY_RECOGNITION);
        } else {
            stepCounter.setupStepCounter();
        }
    }
}
