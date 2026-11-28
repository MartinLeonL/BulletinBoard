package com.example.bulletinboard.models;

import com.google.firebase.Timestamp;

public class Report {
    private String reportId;
    private String targetType; // "event" | "org"
    private String targetId;
    private String reportedByUid;
    private Timestamp timestamp;

    public Report() {
    }

    public Report(String reportId, String targetType, String targetId, String reportedByUid, Timestamp timestamp) {
        this.reportId = reportId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.reportedByUid = reportedByUid;
        this.timestamp = timestamp;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getReportedByUid() {
        return reportedByUid;
    }

    public void setReportedByUid(String reportedByUid) {
        this.reportedByUid = reportedByUid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}