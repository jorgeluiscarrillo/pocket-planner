package com.example.daehe.login;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Daehee on 2018-02-24.
 */

public class Message {
    private String mTitle;
    private User mSender;
    private User mReceiver;
    private Date mDateAndTime;
    private String mContent;
    private boolean mMarkAsImportant;
    private boolean mRead;

    public Message(){
        mTitle = "";
        mSender = new User();
        mReceiver = new User();
        mDateAndTime = new Date();
        mContent = "";
        mMarkAsImportant = false;
        mRead = false;
    }

    public Message(String title, User sender, User receiver, String content){
        mTitle = title;
        mSender = sender;
        mReceiver = receiver;
        mDateAndTime = new Date();
        mContent = content;
        mMarkAsImportant = false;
        mRead = false;
    }

    public String getTitle() {return mTitle;}
    public User getSender() {return mSender;}
    public User getReceiver() {return mReceiver;}
    public Date getDateAndTiem() {return mDateAndTime;}
    public String getContent() {return mContent;}
    public boolean isImportant() {return mMarkAsImportant;}
    public boolean isRead() {return mRead;}

    public void setTitle(String title) {mTitle = title;}
    public void setSender(User sender) {mSender = sender;}
    public void setReceiver(User receiver) {mReceiver = receiver;}
    public void setDateAndTime(Date dateAndTime) {mDateAndTime = dateAndTime;}
    public void setContent(String content) {mContent = content;}
    public void setImportant(boolean important) {mMarkAsImportant = important;}
    public void setRead(boolean read) {mRead = read;}
}
