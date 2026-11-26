package com.example.bulletinboard.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.bulletinboard.database.entity.Event;

import java.util.List;

@Dao
public interface EventDAO
{
    @Insert
    void insertPlayer(Event p);

    @Query("SELECT * FROM Event WHERE eventID = :id")
    Event getEvent(int id);

    @Query("SELECT * FROM Event")
    List<Event> getAllEvents();
}
