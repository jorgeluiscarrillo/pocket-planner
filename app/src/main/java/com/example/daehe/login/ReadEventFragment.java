package com.example.daehe.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Bryan on 3/5/2018.
 */

public class ReadEventFragment extends Fragment {
    View myView;
    Event event;
    TextView eventName;
    TextView eventOwner;
    TextView eventLoc;
    TextView eventDate;
    TextView eventDesc;
    Button bUpdate;
    Button bDelete;
    FirebaseFirestore db;

    @Nullable
    @NonNull
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.fragment_read_event, container,false);

        eventName = (TextView) myView.findViewById(R.id.re_eventName);
        eventOwner = (TextView) myView.findViewById(R.id.re_eventOwner);
        eventLoc = (TextView) myView.findViewById(R.id.re_eventLoc);
        eventDate = (TextView) myView.findViewById(R.id.re_eventDate);
        eventDesc = (TextView) myView.findViewById(R.id.re_eventDes);

        bUpdate = (Button) myView.findViewById(R.id.update_event);
        bDelete = (Button) myView.findViewById(R.id.delete_event);

        DateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy hh:mm aa zzz ");
        db = FirebaseFirestore.getInstance();

        if(event != null)
        {
            eventName.setText(event.getName());
            eventOwner.setText(event.getOwner());
            eventLoc.setText(event.getLocation());
            eventDate.setText(dateFormat.format(event.getDate()));
            eventDesc.setText("Description goes here");
        }

        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.contentframe, new UpdateFragment(), "Update")
                        .addToBackStack(null)
                        .commit();
            }
        });

        bDelete.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Deleting event");
                builder.setMessage("Are you sure you want to delete this event?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pos = MainActivity.events.indexOf(event);

                                db.collection("Events")
                                        .document("user")
                                        .collection("Events")
                                        .document(MainActivity.ids.get(pos))
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(getContext(),"Event deleted!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Toast.makeText(getContext(),"Event could not be deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                MainActivity.events.remove(pos);
                                MainActivity.ids.remove(pos);

                                FragmentManager fm = getFragmentManager();
                                fm.popBackStack();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return myView;
    }

    public void setEvent(Event ev)
    {
        event = ev;
    }
    public Event getEvent() { return event; }
}
