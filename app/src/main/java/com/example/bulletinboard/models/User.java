package com.example.bulletinboard.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String accountType; // "student" | "org"
    private String email;
    private String authEmail;
    private String firstName;
    private String lastName;
    private String username;
    private String usernameLower;
    private List<String> followedOrgs;
    private String orgName;
    private String orgNameLower;
    private String description;
    private int followerCount;

    public User() {
        this.followedOrgs = new ArrayList<>();
    }

    public User(String uid, String accountType, String email, String firstName, String lastName,
                String username, String usernameLower) {
        this.uid = uid;
        this.accountType = accountType;
        this.email = email;
        this.authEmail = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.usernameLower = usernameLower;
        this.followedOrgs = new ArrayList<>();
    }

    public User(String uid, String accountType, String email, String orgName, String orgNameLower,
                String description, int followerCount) {
        this.uid = uid;
        this.accountType = accountType;
        this.email = email;
        this.authEmail = email;
        this.orgName = orgName;
        this.orgNameLower = orgNameLower;
        this.description = description;
        this.followerCount = followerCount;
        this.followedOrgs = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthEmail() {
        return authEmail != null ? authEmail : email;
    }

    public void setAuthEmail(String authEmail) {
        this.authEmail = authEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameLower() {
        return usernameLower;
    }

    public void setUsernameLower(String usernameLower) {
        this.usernameLower = usernameLower;
    }

    public List<String> getFollowedOrgs() {
        return followedOrgs;
    }

    public void setFollowedOrgs(List<String> followedOrgs) {
        this.followedOrgs = followedOrgs;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgNameLower() {
        return orgNameLower;
    }

    public void setOrgNameLower(String orgNameLower) {
        this.orgNameLower = orgNameLower;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}