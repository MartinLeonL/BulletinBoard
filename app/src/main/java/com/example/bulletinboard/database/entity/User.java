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

    public User(String FirstName, String LastName, String Username, String Email, long Phone)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Username = Username;
        this.Email = Email;
        this.Phone = Phone;
    }
}
