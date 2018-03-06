package com.example.daehe.login;

/**
 * Created by Daehee on 2018-02-24.
 */

public class User {
    private String mName;
    private String mEmail;


    public User(){
        mName = "";
        mEmail = "";
    }

    public User(String name, String email){
        mName = name;
        mEmail = email;
    }

    public String getName() {return mName;}
    public String getEmail() {return mEmail;}

    public void setName(String name) {mName = name;}
    public void setEmail(String email) {mEmail = email;}

    @Override
    public String toString(){
        return mName + " (" + mEmail + ")";
    }
}
