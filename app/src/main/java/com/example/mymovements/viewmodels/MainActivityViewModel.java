package com.example.mymovements.viewmodels;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.repositories.MainRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel
{
    private MainRepository mAppRepo; // a reference to the repository for movement logs

    private LiveData<List<RecordedMovement>> mAllRecMovs;

    public MainActivityViewModel(@NonNull Application application)
    {
        super(application);

        mAppRepo = new MainRepository(application); // instantiate the repository

        mAllRecMovs = mAppRepo.getAllRecordedMovements();
    }

    public LiveData<List<RecordedMovement>> getAllRecMovs() { return this.mAllRecMovs; }
    public LiveData<RecordedMovement> getRecMovWithHighestSpeedByDate(String date) { return mAppRepo.getRecMovWithHighestSpeedByDate(date); }
    public LiveData<List<RecordedMovement>> getAllRecMovsByDate(String date) { return mAppRepo.getAllRecMovByDate(date); }
}