package com.example.userspart.CustomTypes;

import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.sql.Time;
import java.util.Date;

@IgnoreExtraProperties
public class Event {
    public String date;
    public String eventName;
    public String location;
    public String time;
    public String uid;



    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Event(String date, String eventName, String location, String time,String uid) {
        this.eventName = eventName;
        this.time=time;
        this.location=location;
        this.date=date;
        this.uid=uid;
    }

}