package com.example.mymovements.contentprovider;

import android.net.Uri;

public class MyMovementsProviderContract 
{
    public static final String AUTHORITY = "com.example.mymovments.contentprovider.RecordedMovementProvider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri RECORDEDMOVEMENTS_URI = Uri.parse("content://"+AUTHORITY+"/recordedmovement/");
    public static final Uri MOVEMENTCOORDINATES_URI = Uri.parse("content://"+AUTHORITY+"/movementcoordinates/");

    //field names
    public static final String RECORDEDMOVEMENT_ID = "_id";
    public static final String RECORDEDMOVEMENT_NAME = "name";
    public static final String RECORDEDMOVEMENT_DATE = "date";
    public static final String RECORDEDMOVEMENT_DISTANCE = "distance";
    public static final String RECORDEDMOVEMENT_TIME = "time";
    public static final String RECORDEDMOVEMENT_SPEED = "speed";
    public static final String RECORDEDMOVEMENT_NOTE = "note";
    public static final String RECORDEDMOVEMENT_IMAGE = "image";

    public static final String MOVEMETCOORDINATES_ID = "_id";
    public static final String MOVEMENTCOORDINATES_RECMOVID = "recordedmovement_id";
    public static final String MOVEMETCOORDINATES_LAT = "lat";
    public static final String MOVEMETCOORDINATES_LNG = "lng";


    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/recordedmovements.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/recordedmovements.data.text";
}
