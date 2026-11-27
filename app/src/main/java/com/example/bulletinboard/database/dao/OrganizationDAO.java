package com.example.bulletinboard.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.bulletinboard.database.relations.*;
import com.example.bulletinboard.database.entity.*;

import java.util.List;

@Dao
public interface OrganizationDAO
{
    @Insert
    long insert(Organization organization);

    @Update
    void update(Organization organization);

    @Delete
    void delete(Organization organization);

    @Transaction
    @Query("SELECT * FROM Organization")
    List<OrganizationWithDetails> getAllOrganizations();

    @Transaction
    @Query("SELECT * FROM Organizer")
    List<Organizer> getAllOrganizers();

    @Transaction
    @Query("SELECT * FROM Organization WHERE OrganizationID = :id")
    OrganizationWithDetails getOrganization(int id);

    @Transaction
    @Query("SELECT * FROM Organization WHERE Name = :name LIMIT 1")
    OrganizationWithDetails getOrganization(String name);

    @Insert
    void addFollower(Follower follower);

    @Insert
    void addOrganizer(Organizer organizer);
}