package com.avichai98.farmroad.Utilities;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyLocationManager implements LocationListener {
    public static int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    // default location Las Vegas
    private double latitude = 36.114704;
    private double longitude = -115.201462;
    private final LocationManager locationManager;

    public MyLocationManager(AppCompatActivity activity){
        locationManager= (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION
            );
        }
        startLocationUpdates(activity);
    }

    private void startLocationUpdates(AppCompatActivity activity) {
        // Check permission again before requesting updates
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        stopLocationTrack();
    }

    public void updateLocation(AppCompatActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                stopLocationTrack();
            }
        }
        else{
            latitude = 36.114704;
            longitude = -115.201462;
        }

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private void stopLocationTrack() {
        locationManager.removeUpdates(this);
    }
}
