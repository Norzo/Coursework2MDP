package com.example.mymovements.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// an entity that is an object for the specific recorded movement activity, this meaning if the user has asked the application to record a specific time to record movement
@Entity (tableName = RecordedMovement.TABLE_NAME)
public class RecordedMovement
{
    public static final String TABLE_NAME = "recordedmovementtbl";

    // a primary key id to be able to differentiate from the different recorded movement entries. one day can have several recorded movement entries
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    @NonNull
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    // the relevant date of the recorded movement
    @ColumnInfo(name = "date")
    @NonNull
    private String date;

    // distance traveled during the recorded movement
    @ColumnInfo(name = "distance")
    @NonNull
    private float distance;

    // the time that elapsed during the recorded movement
    @ColumnInfo(name = "time")
    @NonNull
    private String time;

    // the average speed (km/h) for the recorded movement
    @ColumnInfo(name = "speed")
    @NonNull
    private double speed;

    // field if user wants to annotate run
    @ColumnInfo(name = "note")
    private String note;

    // this field will allow the entry to be able to attach an image to the recorded movement. room persistance library does not allow you to store image files directly, therefore we apply a conversion to
    // an array of bytes which will allow us to store images in the table
    @ColumnInfo(name = "image")
    private String image;

    public RecordedMovement(String name, String date, float distance, String time, double speed, String image, String note)
    {
        this.name = name;
        this.date = date;
        this.distance = distance;
        this.time = time;
        this.speed = speed;
        this.image = image;
        this.note = note;
    }

    public void setName(String name) { this.name = name; }
    public void setId(int id) { this.id = id; }
    public void setDistance(float distance) { this.distance = distance; }
    public void setTime(String time) { this.time = time; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setNote(String note) { this.note = note; }
    public void setImage(String image) { this.image = image; }

    public int getId() { return this.id; }
    public String getName() { return this.name; }
    public String getDate() { return this.date; }
    public float getDistance() { return this.distance; }
    public String getTime() { return this.time; }
    public double getSpeed() { return this.speed; }
    public String getImage() { return this.image; }
    public String getNote() { return this.note; }
}
