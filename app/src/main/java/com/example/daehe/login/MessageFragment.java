package com.example.daehe.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_message,container,false);

        loadInbox();

        //loadButton();

        return myView;
    }

    private void loadInbox(){
        ListView lvInbox = (ListView) myView.findViewById(R.id.lvInbox);
        ArrayList<Message> msgs = new ArrayList<Message>();
        msgs.add(
                new Message(
                        "Test",
                        new User("Daehee", "email@email.com"),
                        new User(),
                        "Test"
                )
        );

        MessageAdapter adapter = new MessageAdapter(getActivity(), msgs);

        lvInbox.setAdapter(adapter);

        lvInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getActivity().getSupportFragmentManager().beginTransaction( )
                        .replace(R.id.contentframe, new ReadMessageFragment())
                        .commit();
            }
        });
    }

    private void loadButton(){
        Button btn = (Button) myView.findViewById(R.id.ibCompose);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction( )
                        .replace(R.id.contentframe, new ReadMessageFragment())
                        .commit();
            }
        });
    }
}
