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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
    PEActionBarActivity activity;
    Event event;
    TextView eventName;
    TextView eventOwner;
    TextView eventLoc;
    TextView eventDate;
    TextView eventDesc;
    TextView eventCode;
    Button bUpdate;
    Button bDelete;
    Button bView;
    Button bStop;
    FirebaseFirestore db;

    @Nullable
    @NonNull
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        myView = inflater.inflate(R.layout.fragment_read_event, container,false);
        activity = (PEActionBarActivity) getActivity();
        eventName = (TextView) myView.findViewById(R.id.re_eventName);
        eventOwner = (TextView) myView.findViewById(R.id.re_eventOwner);
        eventLoc = (TextView) myView.findViewById(R.id.re_eventLoc);
        eventDate = (TextView) myView.findViewById(R.id.re_eventDate);
        eventDesc = (TextView) myView.findViewById(R.id.re_eventDes);
        eventCode = (TextView) myView.findViewById(R.id.re_eventCode);

        bUpdate = (Button) myView.findViewById(R.id.update_event);
        bDelete = (Button) myView.findViewById(R.id.delete_event);
        bView = (Button) myView.findViewById(R.id.view_attendants);
        bStop = (Button) myView.findViewById(R.id.stop_attending);


        DateFormat dateFormat = new SimpleDateFormat("EEEE MMMM dd, yyyy hh:mm aa zzz ");
        db = FirebaseFirestore.getInstance();

        if(event != null)
        {
            eventName.setText(event.getName());
            eventOwner.setText(event.getOwner());
            eventLoc.setText(event.getLocation());
            eventDate.setText(dateFormat.format(event.getDate()));
            eventDesc.setText(event.getDescription());
            eventCode.setText("Join Code: " + event.getKey());
        }

        Toast.makeText(getContext(),event.getOwner(), Toast.LENGTH_SHORT);
        if(!(event.getOwner().equals(activity.getUser().getName())))
        {
            bUpdate.setVisibility(View.GONE);
            bDelete.setVisibility(View.GONE);
            bStop.setVisibility(View.VISIBLE);
        }
        else
        {
            bStop.setVisibility(View.GONE);
        }

        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                alert.setTitle("Viewing attendants");

                if(event.getAttendants().size() == 0)
                {
                    sb.append("No attendees going to this event");
                }
                else
                {
                    for(int i = 0; i < event.getAttendants().size(); i++)
                    {
                        if(i != event.getAttendants().size() - 1)
                        {
                            sb.append(event.getAttendants().get(i).getName() + "\n");
                        }
                        else
                        {
                            sb.append(event.getAttendants().get(i).getName());
                        }
                    }
                }

                alert.setMessage(sb);
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alert.show();
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Stop attending this event");
                builder.setMessage("Are you sure you want to stop attending this event?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0; i < event.getAttendants().size(); i++)
                        {
                            if(event.getAttendants().get(i).getID().equals(activity.getUser().getID()))
                            {
                                event.getAttendants().remove(i);
                            }
                        }
                        int pos = activity.GetEventByCode(event.getKey());

                        if(activity.GetGoogleSignIn())
                        {
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                            db.collection("Events")
                                    .document(acct.getId())
                                    .collection("Events")
                                    .document(activity.GetAllEventIds().get(pos))
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
                        }

                        if(activity.GetFacebookSignIn())
                        {
                            db.collection("Events")
                                    .document(LoginActivity.GetFacebookID())
                                    .collection("Events")
                                    .document(activity.GetAllEventIds().get(pos))
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
                        }
                        db.collection("All Events")
                                .document(activity.GetAllEventIds().get(pos))
                                .set(event);
                        FragmentManager fm = getFragmentManager();
                        activity.getView().notifyData();
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
                                int pos = activity.GetEventByCode(event.getKey());

                                if(activity.GetGoogleSignIn())
                                {
                                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                                    db.collection("Events")
                                            .document(acct.getId())
                                            .collection("Events")
                                            .document(activity.GetAllEventIds().get(pos))
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
                                }

                                if(activity.GetFacebookSignIn())
                                {
                                    db.collection("Events")
                                            .document(LoginActivity.GetFacebookID())
                                            .collection("Events")
                                            .document(activity.GetAllEventIds().get(pos))
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
                                }
                                db.collection("All Events")
                                        .document(activity.GetAllEventIds().get(pos))
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
                                FragmentManager fm = getFragmentManager();
                                activity.getView().notifyData();
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
