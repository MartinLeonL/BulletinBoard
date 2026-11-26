package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User
{
    @PrimaryKey(autoGenerate = true)
    public int UserID;

    public String FirstName;
    public String LastName;
    public String Username;
    public String Email;
    public long Phone;

    public User(String firstName, String lastName, String username, String email, long phone)
    {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Username = username;
        this.Email = email;
        this.Phone = phone;
    }
}
