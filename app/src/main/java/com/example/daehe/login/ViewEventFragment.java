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
        if(activity.GetGoogleSignIn())
        {

            db.collection("Events")
                    .document(activity.getGoogleId())
                    .collection("Events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            events.clear();
                            //Toast.makeText(getApplicationContext(),"ID: " + ids.get(0), Toast.LENGTH_SHORT).show();
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Event> types = documentSnapshots.toObjects(Event.class);
                            // Add all to your list
                            events.addAll(types);
                            loadEventRecycler();
                            Log.d(TAG, "onSuccess: " + events);
                        }
                    });
        }

        if(activity.GetFacebookSignIn())
        {
            db.collection("Events")
                    .document(LoginActivity.GetFacebookID())
                    .collection("Events")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            List<Event> types = documentSnapshots.toObjects(Event.class);
                            // Add all to your list
                            events.addAll(types);
                            loadEventRecycler();
                            Log.d(TAG, "onSuccess: " + events);
                        }
                    });
        }

        return myView;
    }

    public void loadEvents()
    {
        ListView eventList = (ListView) myView.findViewById(R.id.event_list);

        //Toast.makeText(getContext(), "2. There are " + MainActivity.events.size() + " events", Toast.LENGTH_SHORT).show();
        ViewEventAdapter adapter = new ViewEventAdapter(getActivity(),activity.GetEvents());
        eventList.setAdapter(adapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReadEventFragment ref = new ReadEventFragment();
                ref.setEvent(activity.getEventFromList(i));
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentframe,ref, "Read Event")
                        .addToBackStack(null)
                        .commit();
            }
        });
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
