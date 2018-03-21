package com.example.daehe.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.daehe.login.Event;
import com.example.daehe.login.MainActivity;
import com.example.daehe.login.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Bryan on 3/20/2018.
 */

public class UpdateFragment extends Fragment {
    View myView;
    private String eventName;
    private String eventLoc;
    private String eventDate;
    private String eventTime;
    private Activity c;
    private Dialog d;
    private FirebaseFirestore db;
    private TextView title;
    private EditText date;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText name;
    private EditText location;
    private EditText startTime;
    private Button confirm;
    private Event updatedEvent;
    private FragmentManager fragMan;
    private PEActionBarActivity activity;
    private Context mContext;
    private static final int PLACE_PICKER_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.fragment_create_event,container,false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (PEActionBarActivity) getActivity();
        db = FirebaseFirestore.getInstance();
        fragMan = getFragmentManager();
        updatedEvent = ((ReadEventFragment) fragMan.findFragmentByTag("Read Event")).getEvent();
        title = (TextView) view.findViewById(R.id.dialog_title);
        name=(EditText) view.findViewById(R.id.input_name);
        location=(EditText) view.findViewById(R.id.input_location);
        date =(EditText) view.findViewById(R.id.input_date);
        startTime = (EditText) view.findViewById(R.id.input_sTime);
        confirm=(Button) view.findViewById(R.id.event_button);

        String dateFormat = "MM/dd/yy";
        String timeFormat = "hh:mm aa";
        Date eDate = updatedEvent.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        SimpleDateFormat sdfTime = new SimpleDateFormat(timeFormat, Locale.US);

        title.setText("Update Event");
        name.setText(updatedEvent.getName());
        location.setText(updatedEvent.getLocation());
        date.setText(sdf.format(eDate));
        startTime.setText(sdfTime.format(eDate));

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
                new DatePickerDialog(c, datePick, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(c, timePick, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
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

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName = name.getText().toString();
                eventLoc = location.getText().toString();
                eventDate = date.getText().toString();
                eventTime = startTime.getText().toString();
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
                    updatedEvent.setName(eventName);
                    updatedEvent.setLocation(eventLoc);
                    updatedEvent.setDate(inputDate);

                    int pos = activity.GetEvents().indexOf(updatedEvent);

                    if(LoginActivity.mGoogleApiClient != null)
                    {
                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
                        db.collection("Events")
                                .document(acct.getId())
                                .collection("Events")
                                .document(activity.GetEventIds().get(pos))
                                .set(updatedEvent);
                    }

                    if(LoginActivity.isLoggedInFB())
                    {
                        db.collection("Events")
                                .document(LoginActivity.GetFacebookID())
                                .collection("Events")
                                .document(activity.GetEventIds().get(pos))
                                .set(updatedEvent);
                    }
                }
                fragMan.popBackStack();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(mContext, data);
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

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
