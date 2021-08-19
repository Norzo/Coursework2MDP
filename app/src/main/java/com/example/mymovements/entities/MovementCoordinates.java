package com.example.mymovements.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// entity for storing the lat and lng variables of a run, also has a column to connect the entries to a recorded movement entry
@Entity (tableName = MovementCoordinates.tableName,
        foreignKeys = {
        @ForeignKey(entity = RecordedMovement.class,
        parentColumns = "_id",
        childColumns = "recordedmovement_id")
})
public class MovementCoordinates
{
    public static final String tableName = "movementcoordinatestbl";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    private int id;

    // one to many relationship, foreign key reference to recorded movement
    @ColumnInfo(name = "recordedmovement_id")
    @NonNull
    private int recordedMovementId;

    @NonNull
    @ColumnInfo(name = "lat")
    private double lat;

    @NonNull
    @ColumnInfo(name = "lng")
    private double lng;

    public MovementCoordinates(int recordedMovementId, double lat, double lng)
    {
        this.recordedMovementId = recordedMovementId;
        this.lat = lat;
        this.lng = lng;
    }

    public void setId(int id) { this.id = id; }

    public int getRecordedMovementId() { return this.recordedMovementId; }
    public int getId() { return this.id; }
    public double getLat() { return this.lat; }
    public double getLng() { return this.lng; }
}
