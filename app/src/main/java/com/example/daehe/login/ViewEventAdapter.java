package com.example.daehe.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Bryan on 3/4/2018.
 */

public class
ViewEventAdapter extends ArrayAdapter<Event> {
    public ViewEventAdapter(Context c, ArrayList<Event> e)
    {
        super(c,0,e);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Event event = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_event_layout,parent,false);
        }

        TextView evName = (TextView) convertView.findViewById(R.id.ve_eventName);
        TextView evDate = (TextView) convertView.findViewById(R.id.ve_eventDate);
        TextView evFiller = (TextView) convertView.findViewById(R.id.ve_Filler);
        TextView evLocation = (TextView) convertView.findViewById(R.id.ve_eventLoc);

        evName.setText(event.getName());
        evDate.setText(String.format("%tD", event.getDate()));
        evLocation.setText(event.getLocation());
        evFiller.setText(String.format("%tR", event.getDate()));

        return convertView;
    }
}
