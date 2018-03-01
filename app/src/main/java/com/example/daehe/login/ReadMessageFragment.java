package com.example.daehe.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class ReadMessageFragment extends Fragment {
    View myView;
    Message mMsg;
    Boolean isReadOnly = false;
    EditText etSender;
    EditText etReceiver;
    EditText etTitle;
    EditText etDateTime;
    EditText etBody;
    Button bBack;
    Button bReply;
    Button bSend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_read_message,container,false);

        etSender = (EditText) myView.findViewById(R.id.etSender);
        etReceiver = (EditText) myView.findViewById(R.id.etReceiver);
        etTitle = (EditText) myView.findViewById(R.id.etTitle);
        etDateTime = (EditText) myView.findViewById(R.id.etDateTime);
        etBody = (EditText) myView.findViewById(R.id.etBody);

        bBack = (Button) myView.findViewById(R.id.bBack);
        bReply = (Button) myView.findViewById(R.id.bReply);
        bSend = (Button) myView.findViewById(R.id.bSend);

        readOnly(isReadOnly);

        return myView;
    }

    public void setMessage(Message msg){
        mMsg = msg;
    }

    public void setReadOnly(boolean ro){
        isReadOnly = ro;
    }

    public void readOnly(boolean read){
        if(read) {
            etDateTime.setVisibility(View.VISIBLE);
            bReply.setVisibility(View.VISIBLE);
            bSend.setVisibility(View.GONE);

            etSender.setFocusable(false);
            etSender.setOnClickListener(null);
            etReceiver.setFocusable(false);
            etReceiver.setOnClickListener(null);
            etTitle.setFocusable(false);
            etTitle.setOnClickListener(null);
            etDateTime.setFocusable(false);
            etDateTime.setOnClickListener(null);
            etBody.setFocusable(false);
            etBody.setOnClickListener(null);

            etSender.setText(mMsg.getSender().toString());
            etReceiver.setText(mMsg.getReceiver().toString());
            etTitle.setText(mMsg.getTitle());
            etBody.setText(mMsg.getContent());
            etDateTime.setText(String.format("%tc", mMsg.getDateAndTime()));
        }
        else{
            etDateTime.setVisibility(View.GONE);
            bReply.setVisibility(View.GONE);
            bSend.setVisibility(View.VISIBLE);
        }
    }
}
