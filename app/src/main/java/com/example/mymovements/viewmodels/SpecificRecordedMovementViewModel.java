package com.example.mymovements.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.repositories.MainRepository;
import java.util.List;

public class SpecificRecordedMovementViewModel extends AndroidViewModel
{
    private MainRepository mRepo;

    private LiveData<List<RecordedMovement>> mAllRecordedMovements;

    public SpecificRecordedMovementViewModel(@NonNull Application application)
    {
        super(application);
        mRepo = new MainRepository(application);
        mAllRecordedMovements = mRepo.getAllRecordedMovements();
    }

    public LiveData<RecordedMovement> getRecordedMovementById(int id) { return mRepo.getRecordedMovementById(id); }
    public LiveData<List<MovementCoordinates>> getMovementCoordinatesByRecMovId(int id) { return mRepo.getMovementCoordinatesByRecMovId(id); }

    public void updateRecordedMovement(RecordedMovement recordedMovement) { mRepo.updateRecMov(recordedMovement); }
}
