package com.example.bulletinboard;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bulletinboard.database.dao.EventDAO;
import com.example.bulletinboard.database.entity.Event;

@Database(entities = {Event.class}, version=1)
public abstract class DatabaseHandler extends RoomDatabase
{
    public abstract EventDAO eventDAO();

}
