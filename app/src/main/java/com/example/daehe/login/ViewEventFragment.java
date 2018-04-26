package com.example.daehe.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Bryan on 3/2/2018.
 */

public class ViewEventFragment extends Fragment {
    View myView;
    PEActionBarActivity activity;
    private ArrayList<Event> events;
    private RecyclerView eventRecycler;
    private EventRecyclerAdapter adapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.view_event_recycler,container,false);
        activity = (PEActionBarActivity) getActivity();
        //loadEvents();

        db = FirebaseFirestore.getInstance();
        eventRecycler = (RecyclerView) myView.findViewById(R.id.events);

        events = new ArrayList<>();
         db.collection("All Events")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        events.clear();
                        List<Event> types = documentSnapshots.toObjects(Event.class);

                        ArrayList<Event> before = new ArrayList<>();
                        // Add all to your list
                        before.addAll(types);

                        for (Event event: before)
                        {
                            for(Event your: activity.GetEvents())
                            {
                                if(your.getKey().equals(event.getKey()))
                                {
                                    events.add(event);
                                }
                            }
                        }


                        loadEventRecycler();
                        Log.d(TAG, "onSuccess: " + events);
                    }
                });

        return myView;
    }

    public void loadEventRecycler()
    {
        adapter = new EventRecyclerAdapter(getContext(), events, activity);
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecycler.setAdapter(adapter);
    }

    public void notifyData()
    {
        adapter.notifyDataSetChanged();
    }

}
