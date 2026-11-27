package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Organization
{
    @PrimaryKey(autoGenerate = true)
    public int OrganizationID;

    public int UserID; // Organizer ID

    public String Name;
    public String Type;
    public String Description;

    public Organization(String Name, String Type, String Description)
    {
        this.Name = Name;
        this.Description = Description;
        this.Type = Type;
    }
}
