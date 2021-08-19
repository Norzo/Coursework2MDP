package com.example.mymovements.locationtracking;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymovements.R;
import com.example.mymovements.activities.RecordMovementActivity;
import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.viewmodels.RecordMovementActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MyLocationProvider implements LocationListener
{
    private LocationManager locationManager;
    private FusedLocationProviderClient client; // the client api used to retrieve location
    LocationRequest mLocationRequest; // object for location request
    Location mCurrentLocation;
    private RecordMovementActivityViewModel recordMovementActivityViewModel;
    List<Location> mAllLocations;

    private Context context;

    public MyLocationProvider(Application application, RecordMovementActivityViewModel activityViewModel)
    {
        locationManager = (LocationManager) application.getSystemService(application.getApplicationContext().LOCATION_SERVICE);
        client = LocationServices.getFusedLocationProviderClient(application); // initialize the client

        recordMovementActivityViewModel = activityViewModel;

        mAllLocations = new ArrayList<Location>();

        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 9000, 50, this);
        }
        catch (SecurityException e)
        {
            Log.d("LocationProvider", "failed to request location updates");
        }

        context = application.getApplicationContext();
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        if (location != null && recordMovementActivityViewModel.getRecentRecordedMovement() != 0)
        {
            Location prevLocation = recordMovementActivityViewModel.getPrevLocation();
            recordMovementActivityViewModel.newPrevLocation(location);
            recordMovementActivityViewModel.insertMovementCoordinates(new MovementCoordinates((int) recordMovementActivityViewModel.getRecentRecordedMovement(), location.getLatitude(), location.getLongitude()));
            if (prevLocation != null)
                recordMovementActivityViewModel.addRecordedDistance(prevLocation.distanceTo(location));
        }
    }

    public FusedLocationProviderClient getClient() { return this.client; }
}
