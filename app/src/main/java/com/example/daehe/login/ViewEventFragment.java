package com.example.daehe.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Bryan on 3/2/2018.
 */

public class ViewEventFragment extends Fragment {
    View myView;
    PEActionBarActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_view_event,container,false);
        activity = (PEActionBarActivity) getActivity();
        loadEvents();

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
}
