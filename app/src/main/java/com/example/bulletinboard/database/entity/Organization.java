package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Organization
{
    @PrimaryKey(autoGenerate = true)
    public int OrganizationID;

    public String Name;
    public String Type;
    public String Description;

    public Organization()
    {

    }
}
