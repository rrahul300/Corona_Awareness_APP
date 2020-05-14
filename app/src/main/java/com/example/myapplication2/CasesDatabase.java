package com.example.myapplication2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.Serializable;

@Database(entities = CasesBean.class,exportSchema = false,version = 3)
public abstract class CasesDatabase extends RoomDatabase implements Serializable
{
    private static final String DB_NAME = "casesdb";
    private static CasesDatabase instance;

    public static synchronized CasesDatabase getInstance(Context context) {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),CasesDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract  CasesDAO casesDAO();
}
