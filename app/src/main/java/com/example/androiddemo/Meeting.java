package com.example.androiddemo;

public class Meeting {
    private int meetingID;
    private int meeetingUserID;
    private String meetingTitle;
    private String meetingDate;
    private String meetingStart;
    private String meetingEnd;

    public int getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }

    public int getMeeetingUserID() {
        return meeetingUserID;
    }

    public void setMeeetingUserID(int meeetingUserID) {
        this.meeetingUserID = meeetingUserID;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    public String getMeetingEnd() {
        return meetingEnd;
    }

    public void setMeetingEnd(String meetingEnd) {
        this.meetingEnd = meetingEnd;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDateDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingStart() {
        return meetingStart;
    }

    public void setMeetingStart(String meetingStart) {
        this.meetingStart = meetingStart;
    }
}
