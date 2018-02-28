package com.example.daehe.login;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class ReadMessageFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_read_message,container,false);

        Message msg = new Message(
                "Test",
                new User("Daehee", "email@email.com"),
                new User(),
                "Test"
        );

        EditText etSender = (EditText) myView.findViewById(R.id.etSender);
        EditText etReceiver = (EditText) myView.findViewById(R.id.etReceiver);
        EditText etTitle = (EditText) myView.findViewById(R.id.etTitle);
        EditText etDateTime = (EditText) myView.findViewById(R.id.etDateTime);
        EditText etBody = (EditText) myView.findViewById(R.id.etBody);

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

        etSender.setText(msg.getSender().toString());
        etReceiver.setText(msg.getReceiver().toString());
        etTitle.setText(msg.getTitle());
        etBody.setText(msg.getContent());
        etDateTime.setText(String.format("%tc", msg.getDateAndTime()));

        return myView;
    }
}
