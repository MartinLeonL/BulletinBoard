package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event
{
    @PrimaryKey(autoGenerate = true)
    public int eventID;

    public int Type;

    public String Name;
    public String Description;

    public String Location;
    public double LatitudeLocation;
    public double LongitudeLocation;

    public String ThumbUrl;

    public long EndHour;
    public long EndMinute;

    public Event(int type, String name, String description, String location, String thumbUrl, long endHour, long endMinute)
    {
        this.Name = name;
        this.Description = description;
        this.Location = location;
        this.ThumbUrl = thumbUrl;
        this.EndHour = endHour;
        this.EndMinute = endMinute;
    }
}
