package com.example.mymovements.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovements.R;
import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.locationtracking.MyLocationProvider;
import com.example.mymovements.services.RecordMovementService;
import com.example.mymovements.viewmodels.RecordMovementActivityViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecordMovementActivity extends FragmentActivity implements OnMapReadyCallback
{
    // static variables
    public static final float mapZoomValue = 15f; // constant value for how far the map is going to zoom in
    public static final int SAVE_NEW_RECORD_MOVEMENT = 2; // request code for saving a new recorded movement after run is finished

    private RecordedMovement mRecordedMovement; // local variable for adding data to recorded movement entity

    //room variables
    RecordMovementActivityViewModel mActivityViewModel;
    LiveData<List<MovementCoordinates>> mAllMovementCoordinates;

    // service variable
    private RecordMovementService.MyBinder mService;

    // textview variables
    private TextView textTimeElapsed;
    private TextView textDistanceElapsed;

    // map-related variables
    private MyLocationProvider locationProvider;
    private GoogleMap mMap;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private float recordedDistance = 0;
    private float totalDistance;

    // timer related variables
    private int timeElapsed = 0;
    Handler updateTextViewHandler = new Handler();
    Runnable textViewUpdater = new Runnable() // this time counter is in charge of making sure the textview is correctly shown
    {
        @Override
        public void run()
        {
            if (recordedDistance != mActivityViewModel.getRecordedDistance()) // if the recorded distance value is the same as the recorded distance in repo, that means onlocationchanged has not been called
                                                                                // usually meaning the user has not moved
            {
                recordedDistance = mActivityViewModel.getRecordedDistance();
                if (recordedDistance > 5) // if the recorded distance is less than 5, we can assume it is due to gps inaccuracy
                {
                    mActivityViewModel.getMovementCoordinatesById(mActivityViewModel.getRecentMovementCoordinates()).observe(supportMapFragment, new Observer<MovementCoordinates>()
                    {
                        @Override
                        public void onChanged(MovementCoordinates movementCoordinates)
                        {
                            // if movement coordinates are not null
                            if (movementCoordinates != null)
                            {
                                if (movementCoordinates.getId() == mActivityViewModel.getRecentMovementCoordinates()) // checks if the movement coordinates entry is the same as the recently inserted
                                    // movement coordinates, in later cases, the map could be updated for a previous location due to observer lifecycle
                                {
                                    mMap.clear(); // clear map of markers that have previously been added
                                    MarkerOptions markerOptions = new MarkerOptions(); // new marker to add to show where the user is
                                    LatLng latLng = new LatLng(movementCoordinates.getLat(), movementCoordinates.getLng()); // latlng variable to use for google maps
                                    mMap.addMarker(markerOptions.position(latLng)); // adds the marker to the map
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // moves the camera to the marker
                                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(mapZoomValue).build(); // variable to animate camera
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // zooms the view closer to the marker
                                }
                            }
                        }
                    });

                    totalDistance += recordedDistance; // adds the distance between previous location and new location to the total distance covered
                    double distanceInKm = Math.round((totalDistance * 0.001) * 10.0) / 10.0; // converts the distance to kilometers, also rounds it to only show one decimal
                    String distanceString = distanceInKm + " km"; // string to display distance covered in kilometers
                    textDistanceElapsed.setText(distanceString); // sets the display text
                }
            }

            timeElapsed += 1000; // add 1000 to the total time elapsed, which is measured in milliseconds
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed); // converts the time elapsed into minutes
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed); // converts the time elapsed into seconds
            long hours = TimeUnit.MILLISECONDS.toHours(timeElapsed); // converts the time elapsed into hours
            if (seconds >= 60) // if seconds is more or equal to 60
                seconds = seconds % 60; // rewrite variable to show the modulo of 60 (e.g. 68 seconds = 8 seconds)
            if (minutes >= 60) // if minutes is more or equal to 60
                minutes = minutes % 60; // rewrite variable to show the modulo of 60 (e.g. 72 minutes = 12 minutes)
            String tempString = hours + ":" + minutes + ":" + seconds; // create a string to show time elapsed in the format 0:0:0
            textTimeElapsed.setText(tempString); // sets the textview with the string previously created
            updateTextViewHandler.postDelayed(this, 1000); // 1000 miliseconds is one second, updating the textview for each passing second
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_movement);

        // instantiate viewmodel
        mActivityViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RecordMovementActivityViewModel.class);
        mAllMovementCoordinates = mActivityViewModel.getAllMovementCoordinates(); // instantiate the livedata object for all movement coordinates


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        supportMapFragment.getMapAsync(this);

        // fetch the textview references
        textDistanceElapsed = findViewById(R.id.text_distance_elapsed);
        textTimeElapsed = findViewById(R.id.text_time_elapsed);

        locationProvider = new MyLocationProvider(getApplication(), mActivityViewModel); // initialize the fused provider client

        client = locationProvider.getClient(); // gets the fused location provider client from location provider

        // check if the application has permission to access GPS location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            getLastKnownLocation(); // get last known location if permission is given
        else // if not, request permission to use gps
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    44);

    }

    // service connection for binding service
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mService = (RecordMovementService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { mService = null; }
    };

    public void startRecordingMovement(View view)
    {
        Calendar calendar = Calendar.getInstance(); // gets an object to reference calendar to fetch date
        int day = calendar.get(Calendar.DAY_OF_MONTH); // day of the month
        int month = calendar.get(Calendar.MONTH) + 1; // month of the year, seems like 1st month of the year is 0
        int year = calendar.get(Calendar.YEAR); // year
        String date = day + "/" + month + "/" + year; // create string in the format dd/mm/yy

        int hour = calendar.get(Calendar.HOUR_OF_DAY); // the hour of the day
        int minute = calendar.get(Calendar.MINUTE); // the minute of the hour
        String startTime = hour + ":" + minute; // create string in format 00:00

        final Intent serviceIntent = new Intent(this, RecordMovementService.class); // create intent for service
        startService(serviceIntent); // start the service
        bindService(serviceIntent, serviceConnection, 0); // bind the service to activity

        mRecordedMovement = new RecordedMovement(null, date, 0, startTime, 0, null, null); // create new recorded movement upon starting recording
        mActivityViewModel.insertRecordedMovement(mRecordedMovement); // insert into the database, we do this now in order to get a reference to the id in main repository

        mMap.clear();
        getLastKnownLocation(); // fetch last known location to properly set the map when starting to record movement

        updateTextViewHandler.post(textViewUpdater); // post the runnable previously defined to the handler to start updating time and distance elapsed text views
    }

    public void stopRecordingMovement(View view)
    {
        updateTextViewHandler.removeCallbacks(textViewUpdater); // remove callbacks from handler to stop the handler from updating time elapsed and distance elapsed
        mRecordedMovement.setId((int) mActivityViewModel.getRecentRecordedMovement());
        mRecordedMovement.setDistance(totalDistance); // set the distance covered in the recorded movement object
        long hours = TimeUnit.MILLISECONDS.toHours(timeElapsed);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeElapsed);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeElapsed);
        String timeElapsedString = hours + ":" + minutes + seconds;
        mRecordedMovement.setTime(timeElapsedString);
        double distanceInKm = totalDistance * 0.001; // convert distance covered to kilometers
        float timeInHours = timeElapsed / 3600; // convert time elapsed to amount of hours
        double speed = distanceInKm / timeInHours; // using distanceinkm and timeinhours, we can effectively fetch the average speed for the whole run
        mRecordedMovement.setSpeed(speed); // set the speed in the recorded movement object
        mService.stopRecordingMovement(); // tell the service to remove notification

        Intent intent = new Intent(RecordMovementActivity.this, SaveMovementActivity.class); // create intent for saving the recorded movement
        intent.putExtra("recmovid", mRecordedMovement.getId()); // add the recorded movement id in the intent extras
        startActivityForResult(intent, SAVE_NEW_RECORD_MOVEMENT); // start activity for saving recorded movement
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // occurs when user presses finish in save movement activity, depending on resultCode
        if (requestCode == SAVE_NEW_RECORD_MOVEMENT)
        {
            Log.d("onactivityresult", "save new record movement");
            String recMovName = data.getStringExtra("recmovname");
            String recMovAnnotation = data.getStringExtra("recmovnote");
            String recMovImagePath = data.getStringExtra("recmovphotopath");

            mRecordedMovement.setName(recMovName); // sets the name of the recorded movement object
            mRecordedMovement.setNote(recMovAnnotation); // sets the note of the recorded movement object
            mRecordedMovement.setImage(recMovImagePath);
            mActivityViewModel.updateRecordedMovement(mRecordedMovement); // only when the user has saved the movement, we update it in the database
            Log.d("onactivity result", "insert finished");
        }
        finish();
    }

    // runs when the user has either allowed or disallowed the application to use gps provider services
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLastKnownLocation();
        }
    }

    // method for when the map is ready to be used
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.mMap = googleMap;
    }

    // method for getting last known location, usually only for startup
    public void getLastKnownLocation()
    {
        // if statement to see if the user has given permission to use gps services
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                if (location != null) // in some rare cases, location can be null
                {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); // create latlng object from location
                    MarkerOptions markerOptions = new MarkerOptions(); // create marker
                    mMap.addMarker(markerOptions.position(latLng)); // add marker to map
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // moves the map view "above" the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(mapZoomValue).build(); // camera position used for zooming closer to marker
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); // animate camera to zoom closer to marker
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        unbindService(serviceConnection);
        updateTextViewHandler.removeCallbacks(textViewUpdater);
        super.onDestroy();
    }

}