package com.example.bulletinboard;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity
public class Event
{
    @PrimaryKey(autoGenerate = true)
    int eventID;

    int Type;
    String Name;
    String Description;
    String Location;
    String ThumbUrl;
    int EndHour;
    int EndMinute;

    public Event(int type, String name, String description, String location, String thumbUrl, int endHour, int endMinute)
    {
        this.Name = name;
        this.Description = description;
        this.Location = location;
        this.ThumbUrl = thumbUrl;
        this.EndHour = endHour;
        this.EndMinute = endMinute;
    }
}
