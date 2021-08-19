package com.example.mymovements.repositories;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.LiveData;

import com.example.mymovements.daos.MovementCoordinatesDao;
import com.example.mymovements.daos.RecordedMovementDao;
import com.example.mymovements.database.MainRoomDatabase;
import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.entities.RecordedMovement;

import java.util.List;

public class MainRepository
{
    private RecordedMovementDao recordedMovementDao;
    private MovementCoordinatesDao movementCoordinatesDao;

    private LiveData<List<RecordedMovement>> mAllRecordedMovements;
    private LiveData<List<RecordedMovement>> mAllRecordedMovementsByDay;
    private LiveData<RecordedMovement> recordedMovementWithHighestSpeed;
    private LiveData<List<MovementCoordinates>> mAllMovementCoordinates;
    private long recentEntryMovementCoordinates = 0;
    private long recentRecordedRun = 0;

    private float recordedDistance = 0;
    private Location prevLocation;

    public MainRepository(Application application)
    {
        MainRoomDatabase db = MainRoomDatabase.getDatabase(application);

        recordedMovementDao = db.recordedMovementDao();
        movementCoordinatesDao = db.movementCoordinatesDao();

        mAllRecordedMovements = recordedMovementDao.getAllRecMovs();
    }

    public void insertRecMov(final RecordedMovement recordedMovement)
    {
        MainRoomDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                recentRecordedRun = recordedMovementDao.insert(recordedMovement);
            }
        });
    }

    public void updateRecMov(final RecordedMovement recordedMovement)
    {
        MainRoomDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                recordedMovementDao.update(recordedMovement);
            }
        });
    }

    public LiveData<RecordedMovement> getRecMovWithHighestSpeedByDate(String date)
    {
        MainRoomDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                recordedMovementWithHighestSpeed = recordedMovementDao.getRecMovWithHighestSpeed(date);
            }
        });
        return recordedMovementWithHighestSpeed;
    }

    public LiveData<List<RecordedMovement>> getAllRecMovByDate(String date)
    {
        MainRoomDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mAllRecordedMovementsByDay = recordedMovementDao.getAllRecMovsByDate(date);
            }
        });

        return mAllRecordedMovementsByDay;
    }

    public void insertMovCord(MovementCoordinates movementCoordinates)
    {
        MainRoomDatabase.databaseWriteExecutor.execute(new Runnable()
        {
            @Override
            public void run() { recentEntryMovementCoordinates = movementCoordinatesDao.insert(movementCoordinates); }
        });
    }

    public LiveData<MovementCoordinates> getMovementCoordinatesById(int id)
    {
        return movementCoordinatesDao.getMovementCoordinatesById(id);
    }


    public void addRecordedDistance(float distance) { recordedDistance = distance; }
    public void newPrevLocation(Location newPrevLocation) { this.prevLocation = newPrevLocation; }

    public long getRecentEntryMovementCoordinates() { return this.recentEntryMovementCoordinates; }
    public long getRecentRecordedMovement() { return this.recentRecordedRun; }
    public float getRecordedDistance() { return this.recordedDistance; }
    public Location getPrevLocation() { return this.prevLocation; }

    public LiveData<List<MovementCoordinates>> getMovementCoordinatesByRecMovId(int id) { return movementCoordinatesDao.getMovementCoordinatesByRecMovId(id); }
    public LiveData<RecordedMovement> getRecordedMovementById (int id) { return recordedMovementDao.getRecordedMovementById(id); }
    public LiveData<List<RecordedMovement>> getAllRecordedMovements() { return mAllRecordedMovements; }
    public LiveData<List<MovementCoordinates>> getAllMovementCoordinates() { return mAllMovementCoordinates; }

}
