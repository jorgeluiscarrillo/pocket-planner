package com.example.daehe.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

/**
 * Created by Bryan on 4/22/2018.
 */

public class JoinEventFragment extends Fragment {
    View myView;
    private Context mContext;
    private TextView titleCode;
    private EditText enterCode;
    private Button findEvent;
    private TextView joinEventName;
    private TextView joinEventOwner;
    private TextView joinEventLoc;
    private TextView joinEventTime;
    private TextView joinEventDesc;
    private Button joinEvent;
    private Button clearEvent;
    private PEActionBarActivity activity;
    private FirebaseFirestore db;
    private Event selectedEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_join_event,container,false);
        mContext = getActivity();
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        activity = (PEActionBarActivity) getActivity();
        db = FirebaseFirestore.getInstance();

        titleCode = (TextView) myView.findViewById(R.id.je_EventCode);
        enterCode = (EditText) myView.findViewById(R.id.je_enterCode);
        findEvent = (Button) myView.findViewById(R.id.button_findEvent);
        joinEventName = (TextView) myView.findViewById(R.id.je_eventName);
        joinEventOwner = (TextView) myView.findViewById(R.id.je_eventOwner);
        joinEventLoc = (TextView) myView.findViewById(R.id.je_eventLocation);
        joinEventTime = (TextView) myView.findViewById(R.id.je_eventDate);
        joinEventDesc = (TextView) myView.findViewById(R.id.je_eventDesc);
        joinEvent = (Button) myView.findViewById(R.id.button_join);
        clearEvent = (Button) myView.findViewById(R.id.button_clear);

        findEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(enterCode))
                {
                    enterCode.setError("Code cannot be empty");
                }
                else if(enterCode.getText().length() != 4)
                {
                    enterCode.setError("Code is less than 4 digits");
                }
                else
                {
                    boolean found = false;
                    for(int i = 0; i < activity.GetAllEvents().size(); i++)
                    {
                        if(enterCode.getText().toString().equals(activity.GetAllEvents().get(i).getKey()))
                        {
                            joinEventName.setText(activity.GetAllEvents().get(i).getName());
                            joinEventOwner.setText(activity.GetAllEvents().get(i).getOwner());
                            joinEventLoc.setText(activity.GetAllEvents().get(i).getLocation());
                            joinEventTime.setText(activity.GetAllEvents().get(i).getDate().toString());
                            joinEventDesc.setText(activity.GetAllEvents().get(i).getDescription());
                            found = true;
                            selectedEvent = activity.GetAllEvents().get(i);

                            joinEvent.setVisibility(View.VISIBLE);
                            clearEvent.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    if(!found)
                    {
                        joinEventName.setText("No event with that code was found");
                        joinEventOwner.setText("");
                        joinEventLoc.setText("");
                        joinEventTime.setText("");
                        joinEventDesc.setText("");
                        selectedEvent = null;
                        joinEvent.setVisibility(View.GONE);
                        clearEvent.setVisibility(View.GONE);
                    }
                }
            }
        });

        clearEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinEventName.setText("");
                joinEventOwner.setText("");
                joinEventLoc.setText("");
                joinEventTime.setText("");
                joinEventDesc.setText("");
                selectedEvent = null;
                joinEvent.setVisibility(View.GONE);
                clearEvent.setVisibility(View.GONE);
            }
        });

        joinEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean found = false;
                for(int i = 0; i < activity.GetEvents().size(); i++)
                {
                    if(selectedEvent.getKey().equals(activity.GetEvents().get(i).getKey()))
                    {
                        found = true;
                        AlertDialog alert = new AlertDialog.Builder(mContext).create();
                        alert.setTitle("Same event found");
                        alert.setMessage("You are already attending this event!");
                        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        alert.show();

                        break;
                    }
                }
                if(!found)
                {

                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
