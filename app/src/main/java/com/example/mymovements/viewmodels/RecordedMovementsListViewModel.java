package com.example.mymovements.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.repositories.MainRepository;

import java.util.List;

public class RecordedMovementsListViewModel extends AndroidViewModel
{
    private MainRepository mRepo;

    private LiveData<List<RecordedMovement>> mAllRecordedMovements;

    public RecordedMovementsListViewModel(@NonNull Application application)
    {
        super(application);
        mRepo = new MainRepository(application);
        mAllRecordedMovements = mRepo.getAllRecordedMovements();
    }

    public LiveData<List<RecordedMovement>> getAllRecordedMovements() { return this.mAllRecordedMovements; }
}
