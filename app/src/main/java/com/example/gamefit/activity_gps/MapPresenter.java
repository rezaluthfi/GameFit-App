package com.example.gamefit.activity_gps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.gamefit.R;

public class MapPresenter {

    private final AppCompatActivity activity;
    private final MutableLiveData<Ui> ui = new MutableLiveData<>(Ui.EMPTY);

    private final LocationProvider locationProvider;
    private final StepCounter stepCounter;
    private final PermissionsManager permissionsManager;

    public MapPresenter(AppCompatActivity activity) {
        this.activity = activity;
        this.locationProvider = new LocationProvider(activity);
        this.stepCounter = new StepCounter(activity);
        this.permissionsManager = new PermissionsManager(activity, locationProvider, stepCounter);
    }

    public void onViewCreated() {
        locationProvider.getLiveLocations().observe(activity, locations -> {
            Ui current = ui.getValue();
            ui.setValue(new Ui(current.getFormattedPace(), current.getFormattedDistance(), current.getCurrentLocation(), locations));
        });

        locationProvider.getLiveLocation().observe(activity, currentLocation -> {
            Ui current = ui.getValue();
            ui.setValue(new Ui(current.getFormattedPace(), current.getFormattedDistance(), currentLocation, current.getUserPath()));
        });

        locationProvider.getLiveDistance().observe(activity, distance -> {
            Ui current = ui.getValue();
            String formattedDistance = activity.getString(R.string.distance_value, distance);
            formattedDistance += " m";
            ui.setValue(new Ui(current.getFormattedPace(), formattedDistance, current.getCurrentLocation(), current.getUserPath()));
        });

//        stepCounter.getLiveSteps().observe(activity, steps -> {
//            Ui current = ui.getValue();
//            ui.setValue(new Ui(String.valueOf(steps), current.getFormattedDistance(), current.getCurrentLocation(), current.getUserPath()));
//        });
        stepCounter.getLiveSpeed().observe(activity, speed -> {
            Ui current = ui.getValue();
            // Memformat string dengan menggantikan placeholder (%s) dengan nilai kecepatan aktual
            String formattedSpeed = activity.getString(R.string.speed_value, speed);
            // Menambahkan unit "m/s²" setelah nilai speed
            formattedSpeed += " m/s²";
            ui.setValue(new Ui(formattedSpeed, current.getFormattedDistance(), current.getCurrentLocation(), current.getUserPath()));
        });


    }

    public void onMapLoaded() {
        permissionsManager.requestUserLocation();
    }

    public void startTracking() {
        permissionsManager.requestActivityRecognition();
        locationProvider.trackUser();
        ui.setValue(Ui.EMPTY);
    }

    public void stopTracking() {
        locationProvider.stopTracking();
        stepCounter.unloadStepCounter();
    }

    public MutableLiveData<Ui> getUi() {
        return ui;
    }
}
