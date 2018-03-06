package com.example.daehe.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by daehe on 2/27/2018.
 */

public class MessageAdapter extends ArrayAdapter<Message> {
    public MessageAdapter(Context context, ArrayList<Message> msgs){
        super(context, 0, msgs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        Message msg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inbox_layout, parent, false);
        }
        // Lookup view for data population
        TextView tvSender = (TextView) convertView.findViewById(R.id.tvSender);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        // Populate the data into the template view using the data object
        tvSender.setText(msg.getSender().getName());
        tvDate.setText(String.format("%tD", msg.getDateAndTime()));
        tvTime.setText(String.format("%tR", msg.getDateAndTime()));
        tvTitle.setText(msg.getTitle());
        // Return the completed view to render on screen
        return convertView;
    }
}