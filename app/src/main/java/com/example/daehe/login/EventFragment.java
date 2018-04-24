package com.example.daehe.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.DatePicker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DatePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class EventFragment extends Fragment {
    View myView;
    private String eventName;
    private String eventLoc;
    private String eventDate;
    private String eventTime;
    private String eventDescription="";
    private FirebaseFirestore db;
    private EditText date;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText name;
    private EditText location;
    private EditText startTime;
    private Button confirm;
    private TextView addDescription;
    private PEActionBarActivity activity;
    private LatLng eventLatLng;
    private Context mContext;
    private String lat;
    private String lon;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_create_event,container,false);
        mContext = getActivity();
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (PEActionBarActivity) getActivity();
        db = FirebaseFirestore.getInstance();

        name=(EditText) view.findViewById(R.id.input_name);
        location=(EditText) view.findViewById(R.id.input_location);
        date =(EditText) view.findViewById(R.id.input_date);
        startTime = (EditText) view.findViewById(R.id.input_sTime);
        confirm=(Button) view.findViewById(R.id.event_button);

        final DatePickerDialog.OnDateSetListener datePick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        final TimePickerDialog.OnTimeSetListener timePick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hour);
                myCalendar.set(Calendar.MINUTE, minute);

                upDateTime();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePickerDialog.show();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Place picker please", Toast.LENGTH_SHORT);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage() );
                }
            }
        });

        addDescription = (TextView) view.findViewById(R.id.desc_text);
        addDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Add Description (limit 200 character)");
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(200) });
                input.setSingleLine(false);  //add this
                input.setLines(4);
                input.setMaxLines(5);
                input.setGravity(Gravity.LEFT | Gravity.TOP);
                input.setScroller(new Scroller(getContext()));
                input.setVerticalScrollBarEnabled(true);
                input.setText(eventDescription);
                //input.setMovementMethod(new ScrollingMovementMethod());
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        eventDescription = input.getText().toString();
                        updateDes();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), timePick, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = name.getText().toString();
                eventLoc = location.getText().toString();
                eventDate = date.getText().toString();
                eventTime = startTime.getText().toString();

                boolean duplicate = false;
                String newKey = "";

                do{
                    newKey = UUID.randomUUID().toString().substring(0,4);

                    for(int i = 0; i < activity.GetAllEvents().size(); i++)
                    {
                        if(newKey.equals(activity.GetAllEvents().get(i).getKey()))
                        {
                            duplicate = true;
                        }
                    }
                }while(duplicate);

                if(isEmpty(name))
                {
                    name.setError("Name cannot be empty");
                }
                if(isEmpty(location))
                {
                    location.setError("Location cannot be empty");
                }
                if(isEmpty(date))
                {
                    date.setError("Date cannot be empty");
                }
                if(isEmpty(startTime))
                {
                    startTime.setError("Start time cannot be empty");
                }

                if(!isEmpty(name)&&!isEmpty(location)&&!isEmpty(date)&&!isEmpty(startTime))
                {
                    String myFormat = "MM/dd/yy hh:mm aa";
                    Date inputDate = null;
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String dateTime = eventDate + " " + eventTime;
                    try{
                        inputDate = sdf.parse(dateTime);
                    }
                    catch(ParseException e)
                    {

                    }

                    /*
                    db.collection("Events").document("event").set(e).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c, "Event successfully created!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(c, "ERROR" +e.toString(),
                                            Toast.LENGTH_SHORT).show();
                            Log.d("TAG", e.toString());
                        }
                    });*/

                    if(activity.GetGoogleSignIn())
                    {
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                        Event e = new Event(eventName,eventLoc,inputDate,eventDescription,acct.getDisplayName(), Calendar.getInstance().getTime(), false, lat, newKey, false, new ArrayList<User>());

                        DocumentReference doc = db.collection("All Events")
                                .document();

                        db.collection("All Events")
                                .document(doc.getId())
                                .set(e);

                        db.collection("Events")
                                .document(String.valueOf(acct.getId()))
                                .collection("Events")
                                .document(doc.getId())
                                .set(e);
                    }

                    if(activity.GetFacebookSignIn())
                    {
                        Event e = new Event(eventName,eventLoc,inputDate,eventDescription,LoginActivity.GetDisplayName(), Calendar.getInstance().getTime(), false, lat, newKey, false, new ArrayList<User>());

                        DocumentReference doc = db.collection("All Events")
                                .document();

                        db.collection("All Events")
                                .document(doc.getId())
                                .set(e);

                        db.collection("Events")
                                .document(LoginActivity.GetFacebookID())
                                .collection("Events")
                                .document(doc.getId())
                                .set(e);
                    }

                    FragmentManager fm = getFragmentManager();
                    name.setText("");
                    location.setText("");
                    date.setText("");
                    startTime.setText("");
                    Toast.makeText(getContext(), "Event successfully created!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(mContext, data);
                eventLatLng = place.getLatLng();
                location.setText(place.getAddress());
                lat = place.getLatLng().toString();
            }
        }
    }

    private void updateLabel(){
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }


    private void upDateTime(){
        String myFormat = "hh:mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startTime.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDes(){
        addDescription.setText(eventDescription);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
