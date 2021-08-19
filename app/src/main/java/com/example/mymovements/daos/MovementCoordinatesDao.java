package com.example.mymovements.daos;

import android.database.Cursor;
import android.text.method.MovementMethod;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymovements.entities.MovementCoordinates;

import java.util.List;

@Dao
public interface MovementCoordinatesDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(MovementCoordinates movementCoordinates);

    @Query("SELECT * FROM " + MovementCoordinates.tableName)
    LiveData<List<MovementCoordinates>> getAllMovementCoordinates();

    @Query("SELECT * FROM " + MovementCoordinates.tableName + " WHERE _id =:id")
    LiveData<MovementCoordinates> getMovementCoordinatesById(int id);

    // fetch all movement coordinates where the stored recorded movement id is equal to the passed id
    @Query("SELECT * FROM " + MovementCoordinates.tableName + " WHERE recordedmovement_id =:id")
    LiveData<List<MovementCoordinates>> getMovementCoordinatesByRecMovId(int id);

    //queries for content provider
    @Query("SELECT _id FROM " + MovementCoordinates.tableName + " ORDER BY _id")
    Cursor getId();

    @Query("SELECT recordedmovement_id FROM " + MovementCoordinates.tableName + " ORDER BY recordedmovement_id")
    Cursor getRecordedMovementId();

    @Query("SELECT lat FROM " + MovementCoordinates.tableName + " ORDER BY lat")
    Cursor getLat();

    @Query("SELECT lng FROM " + MovementCoordinates.tableName + " ORDER BY lng")
    Cursor getLng();
}
