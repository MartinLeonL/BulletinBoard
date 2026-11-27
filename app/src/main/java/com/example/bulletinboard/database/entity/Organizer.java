package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"OrganizationID", "UserID"})
public class Organizer // Currently not in use... Not needed
{
    public int OrganizationID;
    public int UserID;
}