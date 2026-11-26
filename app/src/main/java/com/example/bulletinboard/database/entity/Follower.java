package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"OrganizationID", "UserID"})
public class Follower
{
    public int OrganizationID;
    public int UserID;
}
