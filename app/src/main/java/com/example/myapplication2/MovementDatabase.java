package com.example.myapplication2;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.io.Serializable;

@Database(entities = MovementTransaction.class,exportSchema = false,version = 12)
public abstract class MovementDatabase extends RoomDatabase implements Serializable{

    private static final String DB_NAME = "movementdb";
    private static MovementDatabase instance;

    public static synchronized MovementDatabase getInstance(Context context) {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),MovementDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract  MovementDAO movementDAO();

}
