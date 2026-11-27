package com.example.bulletinboard.database.relations;

import java.util.List;
import androidx.room.Embedded;
import androidx.room.Relation;
import androidx.room.Junction;

import com.example.bulletinboard.database.entity.*;

public class OrganizationWithDetails
{
    @Embedded
    public Organization organization;

    @Relation(
            parentColumn = "OrganizationID",
            entityColumn = "OrganizationID"
    )
    public List<Event> events;

    @Relation(
            parentColumn = "OrganizationID",
            entityColumn = "UserID",
            associateBy = @Junction(Follower.class)
    )
    public List<User> followers;

    @Relation(
            parentColumn = "UserID",
            entityColumn = "UserID"
    )
    public User organizer;
}