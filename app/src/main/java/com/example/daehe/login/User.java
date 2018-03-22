package com.example.daehe.login;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daehee on 2018-02-24.
 */

public class User {
    private String id;
    private String name;
    private String email;
    private String image;
    private ArrayList<Message> messages;
    private ArrayList<Event> events;


    public User(){}

    public User(String id, String name, String email, String image, ArrayList<Message> msgs, ArrayList<Event> events){
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        messages = msgs;
        events = events;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getID() {return this.id;}
    public String getName() {return this.name;}
    public String getEmail() {return this.email;}
    public String getImage() {return this.image;}
    public ArrayList<Message> getMessages() {return messages;}
    public ArrayList<Event> getEvents() {return events;}

    @Override
    public String toString(){
        return name + " (" + email + ")";
    }
}
