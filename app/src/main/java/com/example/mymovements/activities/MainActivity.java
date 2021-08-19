package com.example.mymovements.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.mymovements.R;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.viewmodels.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final int NEW_RECORDED_MOVEMENT_REQUEST_CODE = 1; // request code for starting new recorded movement

    private String todaysDate; // global variable to easily target the date of today

    private MainActivityViewModel mActivityViewModel;

    // variables for updating the report in the main activity (not used, look at comment on line 88
    private LiveData<List<RecordedMovement>> mAllRecordedMovements;
    private LiveData<List<RecordedMovement>> mAllRecMovsToday;
    private LiveData<RecordedMovement> liveDataRecMovHighestSpeed;
    private LiveData<RecordedMovement> liveDataRecMovHighestSpeedYesterday;
    private TextView bestRunTextView;
    private TextView compareYesterdayTextView;
    private TextView distanceRunToday;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() // the fab is the icon on the bottom right
        {
            @Override
            public void onClick(View view) // when clicked
            {
                Intent intent = new Intent(MainActivity.this, RecordMovementActivity.class); // open the recordmovement activity with an intent
                startActivityForResult(intent, NEW_RECORDED_MOVEMENT_REQUEST_CODE); // start activity for a result
            }
        });

        Calendar calendar = Calendar.getInstance(); // get calendar instance
        int day = calendar.get(Calendar.DAY_OF_MONTH); // get the day of the month
        int month = calendar.get(Calendar.MONTH) + 1; // get the month of the year and add one since the month counting starts from 0 with the calendar library
        int year = calendar.get(Calendar.YEAR); // get the year
        todaysDate = day + "/" + month + "/" + year; // set up the string to use for todays date

        calendar.add(Calendar.DATE, -1); // set the date in the calendar instance to be yesterday
        String yesterday = new SimpleDateFormat("dd/mm/yyyy").format(calendar.getTime()); // get the date of yesterday in the format dd/mm/yyyy

        // instantiate the view model
        mActivityViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainActivityViewModel.class);

        // instantiate text views
        bestRunTextView = findViewById(R.id.bestruntext);
        compareYesterdayTextView = findViewById(R.id.compareyesterdaytext);
        distanceRunToday = findViewById(R.id.distanceRunTodayText);

        // instantiate live data objects
        mAllRecordedMovements = mActivityViewModel.getAllRecMovs();
        mAllRecMovsToday = mActivityViewModel.getAllRecMovsByDate(todaysDate);
        liveDataRecMovHighestSpeed = mActivityViewModel.getRecMovWithHighestSpeedByDate(todaysDate);
        liveDataRecMovHighestSpeedYesterday = mActivityViewModel.getRecMovWithHighestSpeedByDate(yesterday);
        // below is the implementation for the report that i had the idea of showing in the main activity, although, all of the livedata objects are a null object reference
        /*
        mAllRecMovsToday.observe(this, new Observer<List<RecordedMovement>>()
            {
                @Override
                public void onChanged(List<RecordedMovement> recordedMovements)
                {
                    if (recordedMovements != null)
                    {
                        for (int i = 0; i < recordedMovements.size(); i++)
                            distanceToday += recordedMovements.get(i).getDistance();

                        double distanceInKm = Math.round((distanceToday * 0.001) * 10.0) / 10.0;
                        String totDistString = "Today, you have run " + distanceInKm + " km.";
                        distanceRunToday.setText(totDistString);
                    }
                }
            });

        liveDataRecMovHighestSpeed.observe(this, new Observer<RecordedMovement>()
            {
                @Override
                public void onChanged(RecordedMovement recordedMovement)
                {
                    if (recordedMovement != null)
                    {
                        recMovHighestSpeed = recordedMovement;
                        String bestRunString = "Your best run was completed in " + recordedMovement.getTime() + ", with a pace of " + recordedMovement.getSpeed() + ", covering " + recordedMovement.getDistance() + " km.";
                        bestRunTextView.setText(bestRunString);
                    }
                }
            });

            liveDataRecMovHighestSpeedYesterday.observe(this, new Observer<RecordedMovement>()
            {
                @Override
                public void onChanged(RecordedMovement recordedMovement)
                {
                    if (recordedMovement != null)
                    {
                        getRecMovHighestSpeedYesterday = recordedMovement;
                        if (recMovHighestSpeed != null)
                        {
                            double diffSpeed = Math.abs(recMovHighestSpeed.getSpeed() - recordedMovement.getSpeed());
                            double distanceInKm = Math.round((diffSpeed * 0.001) * 10.0) / 10.0;
                            String tempString;
                            if (recordedMovement.getSpeed() < recMovHighestSpeed.getSpeed())
                                tempString = "Your best run was better than yesterday, with an improvement of " + distanceInKm + " km/h.";
                            else
                                tempString = "Your best run was not better than yesterday, with a difference of " + distanceInKm + " km/h.";
                            compareYesterdayTextView.setText(tempString);
                        }
                        else
                        {
                            String tempString = "You have no runs today, get out running and we can see how well you've done in comparison to yesterday!";
                            compareYesterdayTextView.setText(tempString);
                        }
                    }
                }
            });
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // method to navigate to recyclerview list over recorded movements
    public void navToRecordedMovList(View view)
    {
        Intent intent = new Intent(this, RecordedMovementsListActivity.class);
        startActivity(intent);
    }
}