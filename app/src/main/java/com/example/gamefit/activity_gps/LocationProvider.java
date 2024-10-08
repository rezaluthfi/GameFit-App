package com.example.gamefit.activity_gps;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import com.example.gamefit.activity_gps.constant.Constant;

public class LocationProvider {

    private final AppCompatActivity activity;

    private final com.google.android.gms.location.FusedLocationProviderClient client;

    private final List<LatLng> locations = new ArrayList<>();
    private int distance = 0;

    private final MutableLiveData<List<LatLng>> liveLocations = new MutableLiveData<>();
    private final MutableLiveData<Integer> liveDistance = new MutableLiveData<>();
    private final MutableLiveData<LatLng> liveLocation = new MutableLiveData<>();

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult result) {
            LatLng latLng = new LatLng(result.getLastLocation().getLatitude(), result.getLastLocation().getLongitude());
            LatLng lastLocation = locations.isEmpty() ? null : locations.get(locations.size() - 1);

            if (lastLocation != null) {
                distance += Math.round(SphericalUtil.computeDistanceBetween(lastLocation, latLng));
                liveDistance.setValue(distance);
            }

            locations.add(latLng);
            liveLocations.setValue(new ArrayList<>(locations));
        }
    };

    @SuppressLint("MissingPermission")
    public LocationProvider(AppCompatActivity activity) {
        this.activity = activity;
        this.client = LocationServices.getFusedLocationProviderClient(activity);
    }

    @SuppressLint("MissingPermission")
    public void getUserLocation() {
        client.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                locations.add(latLng);
                liveLocation.setValue(latLng);
                Log.d("LocationProvider", "Location obtained: " + latLng);
            } else {
                Log.e("LocationProvider", "Failed to get location: location is null");
            }
        }).addOnFailureListener(e -> Log.e("LocationProvider", "Error getting location", e));
    }


    @SuppressLint("MissingPermission")
    public void trackUser() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Constant.REFRESH_TIME);
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopTracking() {
        client.removeLocationUpdates(locationCallback);
        locations.clear();
        distance = 0;
    }

    public MutableLiveData<List<LatLng>> getLiveLocations() {
        return liveLocations;
    }

    public MutableLiveData<Integer> getLiveDistance() {
        return liveDistance;
    }

    public MutableLiveData<LatLng> getLiveLocation() {
        return liveLocation;
    }
}
