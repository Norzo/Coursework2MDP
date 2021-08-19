package com.example.mymovements.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.repositories.MainRepository;

import java.util.List;

public class RecordMovementActivityViewModel extends AndroidViewModel
{
    private MainRepository mainRepository;

    private LiveData<List<MovementCoordinates>> mAllMovementCoordinates;

    public RecordMovementActivityViewModel(@NonNull Application application)
    {
        super(application);

        mainRepository = new MainRepository(application);

        mAllMovementCoordinates = mainRepository.getAllMovementCoordinates();
    }

    // fetch objects from room
    public LiveData<List<MovementCoordinates>> getAllMovementCoordinates() { return this.mAllMovementCoordinates; }
    public LiveData<MovementCoordinates> getMovementCoordinatesById(long id) { return mainRepository.getMovementCoordinatesById((int) id); }

    // insert methods
    public void insertMovementCoordinates(MovementCoordinates movementCoordinates) { mainRepository.insertMovCord(movementCoordinates); }
    public void insertRecordedMovement(RecordedMovement recordedMovement) { mainRepository.insertRecMov(recordedMovement); }

    // update methods
    public void updateRecordedMovement(RecordedMovement recordedMovement) { mainRepository.updateRecMov(recordedMovement); }

    // local repository values methods
    public void addRecordedDistance(float distance) { mainRepository.addRecordedDistance(distance); }
    public void newPrevLocation(Location location) { mainRepository.newPrevLocation(location); }

    public long getRecentMovementCoordinates() { return mainRepository.getRecentEntryMovementCoordinates(); }
    public long getRecentRecordedMovement() { return mainRepository.getRecentRecordedMovement(); }
    public float getRecordedDistance() { return mainRepository.getRecordedDistance(); }
    public Location getPrevLocation() { return mainRepository.getPrevLocation(); }
}
