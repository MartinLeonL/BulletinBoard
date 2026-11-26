package com.example.bulletinboard.database.relations;

import java.util.List;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.bulletinboard.database.entity.Follower;
import com.example.bulletinboard.database.entity.Organization;
import com.example.bulletinboard.database.entity.User;

public class OrganizationWithFollowers
{
    @Embedded
    public Organization organization;

    @Relation(
            parentColumn = "OrganizationID",
            entityColumn = "UserID",
            associateBy = @Junction(Follower.class)
    )
    public List<User> followers;
}