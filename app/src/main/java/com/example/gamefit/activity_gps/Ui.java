package com.example.gamefit.activity_gps;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Ui {

    private final String formattedPace;
    private final String formattedDistance;
    private final LatLng currentLocation;
    private final List<LatLng> userPath;

    public static final Ui EMPTY = new Ui("", "", null, null);

    public Ui(String formattedPace, String formattedDistance, LatLng currentLocation, List<LatLng> userPath) {
        this.formattedPace = formattedPace;
        this.formattedDistance = formattedDistance;
        this.currentLocation = currentLocation;
        this.userPath = userPath;
    }

    public String getFormattedPace() {
        return formattedPace;
    }

    public String getFormattedDistance() {
        return formattedDistance;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public List<LatLng> getUserPath() {
        return userPath;
    }
}
