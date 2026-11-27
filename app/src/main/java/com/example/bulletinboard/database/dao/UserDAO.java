package com.example.bulletinboard.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.bulletinboard.database.entity.*;
import com.example.bulletinboard.database.relations.*;

import java.util.List;

@Dao
public interface UserDAO
{
    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE UserID = :id")
    User getUser(int id);

    @Query("SELECT * FROM User WHERE Username = :username LIMIT 1")
    User getUser(String username);

    @Query("Select * FROM User Where Username = :username AND Email = :email LIMIT 1")
    User getUser(String username, String email);

    @Transaction
    @Query("SELECT * FROM User WHERE UserID = :id")
    UserWithDetails getUserWithDetails(int id);

    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithDetails> getAllUsersWithDetails();
}