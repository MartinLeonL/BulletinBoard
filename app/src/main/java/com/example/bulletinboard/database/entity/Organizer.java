package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Organizer
{
    @PrimaryKey
    public int OrganizationID;
    @PrimaryKey
    public int UserID;
}