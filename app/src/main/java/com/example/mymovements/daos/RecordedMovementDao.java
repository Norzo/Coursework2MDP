package com.example.mymovements.daos;

import android.database.Cursor;
import android.icu.text.AlphabeticIndex;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymovements.entities.RecordedMovement;

import java.util.List;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
public interface RecordedMovementDao
{
    // insertion sql
    @Insert
    long insert(RecordedMovement recordedMovement);

    // selecft all recorded movement entries
    @Query("SELECT * FROM " + RecordedMovement.TABLE_NAME)
    LiveData<List<RecordedMovement>> getAllRecMovs();

    @Query("SELECT * FROM " + RecordedMovement.TABLE_NAME + " WHERE _id =:id")
    LiveData<RecordedMovement> getRecordedMovementById(int id);

    @Query("SELECT * FROM " + RecordedMovement.TABLE_NAME + " WHERE date =:date ORDER BY speed DESC LIMIT 1")
    LiveData<RecordedMovement> getRecMovWithHighestSpeed(String date);

    @Query("SELECT * FROM " + RecordedMovement.TABLE_NAME + " WHERE date =:date")
    LiveData<List<RecordedMovement>> getAllRecMovsByDate(String date);

    @Update
    void update(RecordedMovement recordedMovement);

    // delete a specific recorded movement entry
    @Delete
    void deleteRecMovEntry(RecordedMovement recordedMovement);

    // queries for content provider
    @Query("SELECT _id FROM " + RecordedMovement.TABLE_NAME + " ORDER BY _id")
    Cursor getId();

    @Query("SELECT name FROM " + RecordedMovement.TABLE_NAME + " ORDER BY name")
    Cursor getName();

    @Query("SELECT date FROM " + RecordedMovement.TABLE_NAME + " ORDER BY date")
    Cursor getDate();

    @Query("SELECT distance FROM " + RecordedMovement.TABLE_NAME + " ORDER BY distance")
    Cursor getDistance();

    @Query("SELECT speed FROM " + RecordedMovement.TABLE_NAME + " ORDER BY speed")
    Cursor getSpeed();

    @Query("SELECT time FROM " + RecordedMovement.TABLE_NAME + " ORDER BY time")
    Cursor getTime();

    @Query("SELECT note FROM " + RecordedMovement.TABLE_NAME + " ORDER BY note")
    Cursor getNote();

    @Query("SELECT image FROM " + RecordedMovement.TABLE_NAME + " ORDER BY image")
    Cursor getImage();
}
