package com.example.daehe.login;

import java.util.Date;

/**
 * Created by Bryan on 2/27/2018.
 */

public class Event {
    private String name;
    private String Location;
    private Date date;
    private String description;
    private String startTime;
    private String endTime;
    private String owner;

    public Event() {}

    public Event(String n, String l, Date d, String des, String st, String et, String o) {
        name = n;
        Location = l;
        date = d;
        description = des;
        startTime = st;
        endTime = et;
        owner = o;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return Location;
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
}
