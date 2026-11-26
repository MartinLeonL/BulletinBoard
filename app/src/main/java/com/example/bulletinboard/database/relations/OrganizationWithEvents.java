package com.example.bulletinboard.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.bulletinboard.database.entity.Event;
import com.example.bulletinboard.database.entity.Organization;

import java.util.List;

public class OrganizationWithEvents
{
    @Embedded
    public Organization organization;
    @Relation(
            parentColumn = "OrganizationID",
            entityColumn = "OrganizationID"
    )
    public List<Event> events;
}
