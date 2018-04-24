package com.example.daehe.login;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bryan on 4/23/2018.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerHolder> {

    Context mContext;
    ArrayList<Event> mEvent;
    PEActionBarActivity activity;

    public EventRecyclerAdapter(Context c, ArrayList<Event> e, PEActionBarActivity pe)
    {
        mContext = c; mEvent = e; activity = pe;
    }

    @Override
    public EventRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_event_layout,parent,false);
        final EventRecyclerHolder vHolder = new EventRecyclerHolder(v);

        vHolder.eventItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadEventFragment ref = new ReadEventFragment();
                ref.setEvent(activity.getEventFromList(vHolder.getAdapterPosition()));
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentframe,ref, "Read Event")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerHolder holder, int position)
    {
        holder.ve_eventName.setText(mEvent.get(position).getName());
        holder.ve_eventLoc.setText(mEvent.get(position).getLocation());
        holder.ve_eventDate.setText(String.format("%tD", mEvent.get(position).getDate()));
        holder.ve_eventTime.setText(String.format("%tR", mEvent.get(position).getDate()));
    }

    public int getItemCount() { return mEvent.size(); }

    public static class EventRecyclerHolder extends RecyclerView.ViewHolder
    {
        private RelativeLayout eventItem;
        private TextView ve_eventName;
        private TextView ve_eventTime;
        private TextView ve_eventDate;
        private TextView ve_eventLoc;

        public EventRecyclerHolder(View itemView) {
            super(itemView);

            eventItem = (RelativeLayout) itemView.findViewById(R.id.ve_relLayout);
            ve_eventName = (TextView) itemView.findViewById(R.id.ve_eventName);
            ve_eventLoc = (TextView) itemView.findViewById(R.id.ve_eventLoc);
            ve_eventDate = (TextView) itemView.findViewById(R.id.ve_eventDate);
            ve_eventTime = (TextView) itemView.findViewById(R.id.ve_Filler);

        }
    }
}
