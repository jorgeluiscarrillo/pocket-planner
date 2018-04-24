package com.example.daehe.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Bryan on 3/2/2018.
 */

public class ViewEventFragment extends Fragment {
    View myView;
    PEActionBarActivity activity;
    private ArrayList<Event> events;
    private RecyclerView eventRecycler;
    private EventRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.view_event_recycler,container,false);
        activity = (PEActionBarActivity) getActivity();
        events = activity.GetEvents();
        //loadEvents();

        eventRecycler = (RecyclerView) myView.findViewById(R.id.events);

        loadEventRecycler();
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

}
