package com.example.mymovements.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymovements.daos.MovementCoordinatesDao;
import com.example.mymovements.daos.RecordedMovementDao;
import com.example.mymovements.database.MainRoomDatabase;
import com.example.mymovements.entities.MovementCoordinates;

public class RecordedMovementProvider extends ContentProvider
{
    // dao variables
    private RecordedMovementDao recordedMovementDao;
    private MovementCoordinatesDao movementCoordinatesDao;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); // instantiate and create the uri matcher object

    // add uris to the urimatcher
    static
    {
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/id", 1);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/name", 2);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/date", 3);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/distance", 4);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/time", 5);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/speed", 6);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/note", 7);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/image", 8);

        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/movementcoordinates/id", 9);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/movementcoordinates/recordedmovementid", 10);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/movementcoordinates/lat", 11);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/movementcoordinates/lng", 12);

        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/recordedmovement/", 13);
        uriMatcher.addURI(MyMovementsProviderContract.AUTHORITY, "/movementcoordinates/", 14);

    }

    @Override
    public boolean onCreate()
    {
        // instantiate the dao variables
        movementCoordinatesDao = MainRoomDatabase.getDatabase(getContext()).movementCoordinatesDao();
        recordedMovementDao = MainRoomDatabase.getDatabase(getContext()).recordedMovementDao();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        // when query is called, fetch the right data depending on the uri variable
        switch(uriMatcher.match(uri))
        {
            case 1:
                return recordedMovementDao.getId();
            case 2:
                return recordedMovementDao.getName();
            case 3:
                return recordedMovementDao.getDate();
            case 4:
                return recordedMovementDao.getDistance();
            case 5:
                return recordedMovementDao.getTime();
            case 6:
                return recordedMovementDao.getSpeed();
            case 7:
                return recordedMovementDao.getNote();
            case 8:
                return recordedMovementDao.getImage();
            case 9:
                return movementCoordinatesDao.getId();
            case 10:
                return movementCoordinatesDao.getRecordedMovementId();
            case 11:
                return movementCoordinatesDao.getLat();
            case 12:
                return movementCoordinatesDao.getLng();
        }
        return null;
    }

    // method to determine if we want multiple or single content types
    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        String contentType;

        if (uri.getLastPathSegment() == null)
            contentType = MyMovementsProviderContract.CONTENT_TYPE_MULTIPLE;
        else
            contentType = MyMovementsProviderContract.CONTENT_TYPE_SINGLE;
        return contentType;
    }

    // methods below are simply not supported, if called we throw an unsupportedexpectation

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        throw new UnsupportedOperationException("Not implemented yet - read only");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        throw new UnsupportedOperationException("Not implemented yet - read only");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        throw new UnsupportedOperationException("Not implemented yet - read only");
    }
}
