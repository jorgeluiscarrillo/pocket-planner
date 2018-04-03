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
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class ReadMessageFragment extends Fragment {
    private View myView;
    private Message mMsg;
    private Boolean isReadOnly = false;
    private TextView tvSender;
    private EditText etReceiver;
    private EditText etTitle;
    private EditText etDateTime;
    private EditText etBody;
    private Button bBack;
    private Button bReply;
    private Button bSend;
    private User userContext;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_read_message,container,false);

        userContext = ((PEActionBarActivity)getActivity()).getUser();

        tvSender = (TextView) myView.findViewById(R.id.tvSender);
        etReceiver = (EditText) myView.findViewById(R.id.etReceiver);
        etTitle = (EditText) myView.findViewById(R.id.etTitle);
        etDateTime = (EditText) myView.findViewById(R.id.etDateTime);
        etBody = (EditText) myView.findViewById(R.id.etBody);

        bBack = (Button) myView.findViewById(R.id.bBack);
        bReply = (Button) myView.findViewById(R.id.bReply);
        bSend = (Button) myView.findViewById(R.id.bSend);

        db = FirebaseFirestore.getInstance();

        readOnly(isReadOnly);

        setButtonListeners();

        return myView;
    }

    private void setButtonListeners(){
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sender = userContext.getEmail();
                String receiver = etReceiver.getText().toString();
                String title = etTitle.getText().toString();
                String body = etBody.getText().toString();

                Message msg = new Message(title, sender, receiver, body);

                DocumentReference doc = db.collection("Messages").document();

                db.collection("Messages")
                        .document(doc.getId())
                        .set(msg);
            }
        });
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

            tvSender.setFocusable(false);
            etReceiver.setFocusable(false);
            etReceiver.setOnClickListener(null);
            etTitle.setFocusable(false);
            etTitle.setOnClickListener(null);
            etDateTime.setFocusable(false);
            etDateTime.setOnClickListener(null);
            etBody.setFocusable(false);
            etBody.setOnClickListener(null);

            tvSender.setText(mMsg.getSender().toString());
            etReceiver.setText(mMsg.getReceiver().toString());
            etTitle.setText(mMsg.getTitle());
            etBody.setText(mMsg.getContent());
            etDateTime.setText(String.format("%tc", mMsg.getDateAndTime()));
        }
        else{
            tvSender.setText(userContext.getName() + " (" + userContext.getEmail() + ")");
            etDateTime.setVisibility(View.GONE);
            bReply.setVisibility(View.GONE);
            bSend.setVisibility(View.VISIBLE);
        }
    }
}
