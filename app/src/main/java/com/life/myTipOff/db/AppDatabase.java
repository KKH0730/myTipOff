package com.life.myTipOff.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.life.myTipOff.model.Report;

@Database(entities = {Report.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static String DATABASE_NAME = "database";
    private static AppDatabase database;

    public synchronized static AppDatabase getInstance(Context context)
    {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }


    public abstract ReportDao reportDao();
}
