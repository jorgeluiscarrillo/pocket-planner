package com.example.daehe.login;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
    private String owner;
    private Date timeMade;
    private Boolean isPrivate;
    private String lat;
    private String key;
    private Boolean favorite;
    private ArrayList<User> attendants;

    public Event() {}

    public Event(String n, String l, Date d, String des, String o, Date ct, Boolean p, String lt, String k, Boolean fav, ArrayList<User> a) {
        name = n;
        location = l;
        date = d;
        description = des;
        owner = o;
        timeMade = ct;
        isPrivate = p;
        lat = lt;
        key = k;
        favorite = fav;
        attendants = a;
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

    public String getKey()
    {
        return key;
    }

    public Boolean getFavorite()
    {
        return favorite;
    }

    public ArrayList<User> getAttendants()
    {
        return attendants;
    }

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

    public void setKey(String k)
    {
        key = k;
    }

    public void setFavorite(Boolean fav) {
        favorite = fav;
    }

    public void setAttendants(ArrayList<User> a)
    {
        attendants = a;
    }

}
