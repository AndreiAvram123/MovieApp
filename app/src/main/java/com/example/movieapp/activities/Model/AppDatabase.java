package com.example.movieapp.activities.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.movieapp.activities.interfaces.DatabaseInterface;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseInterface databaseInterface();
}
