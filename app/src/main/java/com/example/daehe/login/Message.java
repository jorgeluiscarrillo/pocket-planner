package com.example.daehe.login;

import android.support.annotation.NonNull;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Daehee on 2018-02-24.
 */

public class Message implements Comparable<Message> {
    private String mTitle;
    private String mSender;
    private String mReceiver;
    private Date mDate;
    private String mContent;
    private boolean mMarkAsImportant;
    private boolean mRead;

    public Message(){}

    public Message(String title, String sender, String receiver, String content){
        mTitle = title;
        mSender = sender;
        mReceiver = receiver;
        mDate = new Date();
        mContent = content;
        mMarkAsImportant = false;
        mRead = false;
    }

    public String getTitle() {return mTitle;}
    public String getSender() {return mSender;}
    public String getReceiver() {return mReceiver;}
    public Date getDate() {return mDate;}
    public String getContent() {return mContent;}
    public boolean isImportant() {return mMarkAsImportant;}
    public boolean isRead() {return mRead;}

    public void setTitle(String title) {mTitle = title;}
    public void setSender(String sender) {mSender = sender;}
    public void setReceiver(String receiver) {mReceiver = receiver;}
    public void setDate(Date date) {mDate = date;}
    public void setContent(String content) {mContent = content;}
    public void setImportant(boolean important) {mMarkAsImportant = important;}
    public void setRead(boolean read) {mRead = read;}

    @Override
    public int compareTo(@NonNull Message message) {
        return -mDate.compareTo(message.getDate());
    }
}
