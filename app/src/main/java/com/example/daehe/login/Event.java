package com.example.daehe.login;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bryan on 2/27/2018.
 */

public class Event {
    private String name;
    private String location;
    private Date date;
    private String description;
    private String startTime;
    private String endTime;
    private String owner;
    private Date timeMade;
    private Boolean isPrivate;

    public Event() {}

    public Event(String n, String l, Date d, String des, String st, String et, String o, Date ct, Boolean p) {
        name = n;
        location = l;
        date = d;
        description = des;
        startTime = st;
        endTime = et;
        owner = o;
        timeMade = ct;
        isPrivate = p;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getOwner() {
        return owner;
    }

    public Date getTimeMade() { return timeMade; }

    public Boolean getIsPrivate() {return isPrivate;}

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
