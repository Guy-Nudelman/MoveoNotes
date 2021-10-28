package com.example.moveonotes.Data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.moveonotes.Model.Note;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {
        Note.class
}, version = 8, exportSchema = false)
public abstract class Database extends RoomDatabase {

    //Variables
    private static Database instance;
    private static final String DATABASE_NAME = "MOVEO_DB";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Communication Interface
    public abstract DbInterface dbInterface();


    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }


}
