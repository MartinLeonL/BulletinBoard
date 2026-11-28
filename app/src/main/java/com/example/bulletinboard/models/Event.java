package com.example.bulletinboard.models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String eventId;
    private String name;
    private String category; // "CLUB", "FOOD", "SPORT", "SOCIAL", "STUDENT GOV", "OTHER"
    private String locationLabel;
    private double latitude;
    private double longitude;
    private Timestamp endTime;
    private String description;
    private String photoBase64;
    private String postedByUid;
    private String postedByName;
    private String postedByType; // "student" | "org"
    private Timestamp createdAt;
    private boolean active = true;
    private Map<String, String> votes;

    public Event() {
        this.votes = new HashMap<>();
    }

    public Event(String eventId, String name, String category, String locationLabel,
                 double latitude, double longitude, Timestamp endTime, String description,
                 String photoBase64, String postedByUid, String postedByName,
                 String postedByType, Timestamp createdAt, boolean active) {
        this.eventId = eventId;
        this.name = name;
        this.category = category;
        this.locationLabel = locationLabel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.endTime = endTime;
        this.description = description;
        this.photoBase64 = photoBase64;
        this.postedByUid = postedByUid;
        this.postedByName = postedByName;
        this.postedByType = postedByType;
        this.createdAt = createdAt;
        this.active = active;
        this.votes = new HashMap<>();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public String getPostedByUid() {
        return postedByUid;
    }

    public void setPostedByUid(String postedByUid) {
        this.postedByUid = postedByUid;
    }

    public String getPostedByName() {
        return postedByName;
    }

    public void setPostedByName(String postedByName) {
        this.postedByName = postedByName;
    }

    public String getPostedByType() {
        return postedByType;
    }

    public void setPostedByType(String postedByType) {
        this.postedByType = postedByType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, String> votes) {
        this.votes = votes;
    }
}