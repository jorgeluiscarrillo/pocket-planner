package com.example.daehe.login;

import android.net.Uri;

/**
 * Created by Daehee on 2018-02-24.
 */

public class User {
    private String mName;
    private String mEmail;
    private String mID;
    private Uri mImage;


    public User(){
        mName = "";
        mEmail = "";
        mID = "";
        mImage = null;
    }

    public User(String name, String email, String id, Uri image){
        mName = name;
        mEmail = email;
        mID = id;
        mImage = image;
    }

    public String getName() {return mName;}
    public String getEmail() {return mEmail;}
    public String getID() {return mID;}
    public Uri getImage() {return mImage;}

    @Override
    public String toString(){
        return mName + " (" + mEmail + ")";
    }
}
