package com.example.daehe.login;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bryan on 2/27/2018.
 */

public class Event implements Comparable<Event> {
    private String name;
    private String location;
    private Date date;
    private String description;
    private String owner;
    private Date timeMade;
    private Boolean isPrivate;
    private String lat;

    public Event() {}

    public Event(String n, String l, Date d, String des, String o, Date ct, Boolean p, String lt) {
        name = n;
        location = l;
        date = d;
        description = des;
        owner = o;
        timeMade = ct;
        isPrivate = p;
        lat = lt;
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

    public String getOwner() {
        return owner;
    }

    public Date getTimeMade() { return timeMade; }

    public Boolean getIsPrivate() {return isPrivate;}

    public String getLat() {return lat;}

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setLat(String lt) {lat=lt;}

    @Override
    public int compareTo(@NonNull Event event) {
        return date.compareTo(event.getDate());
    }
}
