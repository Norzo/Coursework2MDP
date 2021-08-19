package com.example.mymovements.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.example.mymovements.R;
import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.viewmodels.SpecificRecordedMovementViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsRouteActivity extends FragmentActivity implements OnMapReadyCallback
{
    private SpecificRecordedMovementViewModel mViewModel;
    LiveData<List<MovementCoordinates>> mMovementCoordinates;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // instantiate viewmodel
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SpecificRecordedMovementViewModel.class);
        mMovementCoordinates = mViewModel.getMovementCoordinatesByRecMovId(getIntent().getIntExtra("recmovid", 1)); // fetch the recmov id from intent
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // observe movement coordinates
        mMovementCoordinates.observe(this, new Observer<List<MovementCoordinates>>()
        {
            @Override
            public void onChanged(List<MovementCoordinates> movementCoordinates)
            {
                PolylineOptions polylineOptions = new PolylineOptions(); // create polylineoptions for adding latlngs
                ArrayList<LatLng> latLngs = new ArrayList<>(); // list for the relevant lat lngs

                for (int i = 0; i < movementCoordinates.size(); i++)
                {
                    MovementCoordinates mMovCord = movementCoordinates.get(i); // fetch the movement coordinate
                    latLngs.add(new LatLng(mMovCord.getLat(), mMovCord.getLng())); // add a new latlng to the array, using the values of the movement coordinates
                }
                polylineOptions.addAll(latLngs); // add the latlng array to the polyline options
                googleMap.addPolyline(polylineOptions); // add the polyline to the map
                LatLng latLng = new LatLng(movementCoordinates.get(0).getLat(), movementCoordinates.get(0).getLng()); // create a latlng from the first entry in the movement coordinates
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15f).build(); // build a cameraposition variable so we can animate camera
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // animate camera and zoom closer to the previously declared latlng
            }
        });
    }
}