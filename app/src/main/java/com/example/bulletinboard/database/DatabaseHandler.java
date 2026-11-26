package com.example.bulletinboard.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bulletinboard.database.dao.EventDAO;
import com.example.bulletinboard.database.dao.OrganizationDAO;
import com.example.bulletinboard.database.dao.UserDAO;
import com.example.bulletinboard.database.entity.*;

@Database(entities = {Event.class, Organization.class, User.class, Follower.class, Organizer.class}, version=1)
public abstract class DatabaseHandler extends RoomDatabase
{
    public abstract EventDAO eventDAO();
    public abstract OrganizationDAO organizationDAO();
    public abstract UserDAO userDAO();
}
