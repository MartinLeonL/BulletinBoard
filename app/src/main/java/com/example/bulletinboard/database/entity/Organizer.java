package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"OrganizationID", "UserID"})
public class Organizer
{
    public int OrganizationID;
    public int UserID;
}