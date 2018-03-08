package com.example.daehe.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bryan on 3/6/2018.
 */

public class UpdateEventDialog extends Dialog {
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

    public UpdateEventDialog(Activity a, Event e,FragmentManager fm)
    {
        super(a);
        this.c = a;
        updatedEvent = e;
        db = FirebaseFirestore.getInstance();
        fragMan = fm;
    }

    public void ShowDialog()
    {
        d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.fragment_create_event);

        title = (TextView) d.findViewById(R.id.dialog_title);
        name=(EditText) d.findViewById(R.id.input_name);
        location=(EditText) d.findViewById(R.id.input_location);
        date =(EditText) d.findViewById(R.id.input_date);
        startTime = (EditText) d.findViewById(R.id.input_sTime);
        confirm=(Button) d.findViewById(R.id.event_button);

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

                    int pos = MainActivity.events.indexOf(updatedEvent);

                    db.collection("Events")
                            .document("user")
                            .collection("Events")
                            .document(MainActivity.ids.get(pos))
                            .set(updatedEvent);
                    d.dismiss();
                }


                fragMan.popBackStack();

                Fragment view = null;
                view = fragMan.findFragmentByTag("View Event");
                final android.support.v4.app.FragmentTransaction ft = fragMan.beginTransaction();
                ft.detach(view);
                ft.attach(view);
                ft.commit();
            }
        });
        d.show();
        Window window = d.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
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
