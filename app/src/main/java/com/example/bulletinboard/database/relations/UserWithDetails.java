package com.example.bulletinboard.database.relations;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;
import androidx.room.Junction;

import com.example.bulletinboard.database.entity.*;

public class UserWithDetails
{
    @Embedded
    public User user;

    @Relation(
            parentColumn = "UserID",
            entityColumn = "OrganizationID",
            associateBy = @Junction(Follower.class)
    )
    public List<Organization> followedOrganizations;

    @Relation(
            parentColumn = "UserID",
            entityColumn = "OrganizationID",
            associateBy = @Junction(Organizer.class)
    )
    public List<Organization> ownedOrganizations;
}