package com.example.bulletinboard.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event
{
    @PrimaryKey(autoGenerate = true)
    public int EventID;

    public int OrganizationID;

    public int Type;

    public String Name;
    public String Description;

    public String Location;
    public double LatitudeLocation;
    public double LongitudeLocation;

    public String ThumbUrl;

    public long EndHour;
    public long EndMinute;

    public Event(int OrganizationID, double LatitudeLocation, double LongitudeLocation, int Type, String Name, String Description, String Location, String ThumbUrl, long EndHour, long EndMinute)
    {
        this.OrganizationID = OrganizationID;
        this.LatitudeLocation = LatitudeLocation;
        this.LongitudeLocation = LongitudeLocation;
        this.Type = Type;
        this.Name = Name;
        this.Description = Description;
        this.Location = Location;
        this.ThumbUrl = ThumbUrl;
        this.EndHour = EndHour;
        this.EndMinute = EndMinute;
    }
}
