package com.example.mymovements.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mymovements.R;
import com.example.mymovements.activities.RecordMovementActivity;
import com.example.mymovements.locationtracking.MyLocationProvider;
import com.example.mymovements.viewmodels.RecordMovementActivityViewModel;

public class RecordMovementService extends Service
{
    public static RecordMovementService instance;
    private static final String SERVICE_ID = "locationTrackerChannel";
    NotificationManager notificationManager;
    private int notificationId = 001;
    private MyBinder binder = new MyBinder();
    private MyLocationProvider locationProvider;
    private RecordMovementActivityViewModel mViewModel;

    public class MyBinder extends Binder
    {
        public void startRecordingMovement()
        {
            createNotification();
        }

        public void stopRecordingMovement()
        {
            removeNotification();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mViewModel = new RecordMovementActivityViewModel(getApplication());
        locationProvider = new MyLocationProvider(getApplication(), mViewModel);

        CharSequence name = "MyMovements Notification";
        String description = "Displays when user is on a run";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(SERVICE_ID, name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);

        Intent intent = new Intent(this, RecordMovementActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent navBackToActivity = PendingIntent.getActivity(this, 0, intent, 0);

        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this, SERVICE_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("MyMovements Tracker")
                .setContentText("Recording run")
                .setContentIntent(navBackToActivity)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(notificationId, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {


        return flags;
    }

    public void createNotification()
    {

    }

    public void removeNotification()
    {
        stopForeground(false);
        notificationManager.cancel(notificationId);
    }

    @Override
    public void onDestroy()
    {
        instance = null;
        stopForeground(false);
        notificationManager.cancel(notificationId);
        super.onDestroy();
    }
}
