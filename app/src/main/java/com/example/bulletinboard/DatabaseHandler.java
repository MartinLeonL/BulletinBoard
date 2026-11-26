package com.example.bulletinboard;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version=1)
public abstract class DatabaseHandler extends RoomDatabase
{
    public abstract EventDAO eventDAO();

}
