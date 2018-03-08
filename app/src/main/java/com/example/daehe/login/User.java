package com.example.daehe.login;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daehee on 2018-02-24.
 */

public class User {
    private String mName;
    private String mEmail;
    private Uri mImage;
    private ArrayList<Message> mMsgs;
    private ArrayList<Event> mEvents;


    public User(){
        mName = "";
        mEmail = "";
        mImage = null;
        mMsgs = new ArrayList<Message>();
        mEvents = new ArrayList<Event>();
    }

    public User(String name, String email, Uri image, ArrayList<Message> msgs, ArrayList<Event> events){
        mName = name;
        mEmail = email;
        mImage = image;
        mMsgs = msgs;
        mEvents = events;
    }

    public String getName() {return mName;}
    public String getEmail() {return mEmail;}
    public Uri getImage() {return mImage;}
    public ArrayList<Message> getMessages() {return mMsgs;}
    public ArrayList<Event> getEvents() {return mEvents;}

    @Override
    public String toString(){
        return mName + " (" + mEmail + ")";
    }
}
