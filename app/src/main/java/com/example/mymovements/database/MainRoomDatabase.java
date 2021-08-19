package com.example.mymovements.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mymovements.daos.MovementCoordinatesDao;
import com.example.mymovements.daos.RecordedMovementDao;
import com.example.mymovements.entities.MovementCoordinates;
import com.example.mymovements.entities.RecordedMovement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {RecordedMovement.class, MovementCoordinates.class}, version = 2, exportSchema = false)
public abstract class MainRoomDatabase extends RoomDatabase
{
    // abstract methods for the repositories to instantiate the daos
    public abstract RecordedMovementDao recordedMovementDao();
    public abstract MovementCoordinatesDao movementCoordinatesDao();

    private static MainRoomDatabase INSTANCE; // making use of the singleton method to make sure that there is only one instance of the database

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4); // an object which allows us to run methods through a thread pool instead of the main ui thread

    // a callback which allows us to run something specific upon creation of the database
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db)
        {
            super.onOpen(db);

            databaseWriteExecutor.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    // implement
                }
            });
        }
    };

    public static MainRoomDatabase getDatabase(final Context context) // method that both returns the instance of the database, and also creates the database if no instance exists
    {
        if (INSTANCE == null)
            synchronized (MainRoomDatabase.class)
            {
                if (INSTANCE == null)
                    // if database doesnt exist, then instantiate instance to become an object that is returned from the Room.databaseBuilder method
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MainRoomDatabase.class, "main_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
            }
        return INSTANCE;
    }
}
