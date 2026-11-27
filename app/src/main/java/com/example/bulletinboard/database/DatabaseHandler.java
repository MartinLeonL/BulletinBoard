package com.example.bulletinboard.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bulletinboard.database.dao.EventDAO;
import com.example.bulletinboard.database.dao.OrganizationDAO;
import com.example.bulletinboard.database.dao.UserDAO;
import com.example.bulletinboard.database.entity.*;
import com.example.bulletinboard.database.relations.OrganizationWithDetails;

import java.util.List;

@Database(entities = {Event.class, Organization.class, User.class, Follower.class, Organizer.class}, version=2)
public abstract class DatabaseHandler extends RoomDatabase
{
    public static User currentUser;

    public abstract EventDAO eventDAO();
    public abstract OrganizationDAO organizationDAO();
    public abstract UserDAO userDAO();

    public static DatabaseHandler build(Context context)
    {
        return Room.databaseBuilder(context,
                    DatabaseHandler.class, "DatabaseHandler")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(true)
            .build();
    }

    public static void loginAs(User user)
    {
        currentUser = user;
    }

    public static void logout()
    {
        currentUser = null;
    }

    public static boolean isOrgAccount(DatabaseHandler ref)
    {
        if(currentUser == null)
            return false;

        OrganizationWithDetails existingOrg = ref.organizationDAO().getOrganization(currentUser.Username);

        if(existingOrg == null)
            return false;
        else
            return existingOrg.organizer.equals(currentUser);
    }
}
